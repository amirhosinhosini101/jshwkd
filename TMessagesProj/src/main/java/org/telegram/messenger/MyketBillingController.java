package org.telegram.messenger;

import android.app.Activity;

import org.json.JSONObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;
import java.util.Locale;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ir.myket.billingclient.IabHelper;
import ir.myket.billingclient.util.Purchase;

/**
 * Myket in-app billing for Gitbin Stars and Gitbin Premium.
 * Flow: launchPurchaseFlow -> send receipt to our server -> server verifies and credits
 * -> consume. A purchase is consumed ONLY after the server accepted it. Unconsumed
 * purchases are re-sent on the next buy (recoverPending), so the server is idempotent
 * per purchaseToken.
 */
public class MyketBillingController {

    private static final String TAG = "MyketBilling";
    private static final int USER_CANCELLED = -1005;

    // Stars: SKU stars_<n> -> n stars. Price in Rial (informational, the server uses its own table).
    private static final long[] STARS = {10, 50, 100, 500, 1000};
    private static final long[] PRICE_IRR = {50000, 250000, 500000, 2500000, 5000000};

    /** One Premium plan sold through Myket (consumable product, the server adds the months). */
    public static class PremiumPlan {
        public final String sku;
        public final int months;
        public final long priceToman;
        public final long oldPriceToman; // 0 = no discount

        PremiumPlan(String sku, int months, long priceToman, long oldPriceToman) {
            this.sku = sku;
            this.months = months;
            this.priceToman = priceToman;
            this.oldPriceToman = oldPriceToman;
        }

        public boolean hasDiscount() {
            return oldPriceToman > priceToman;
        }

        public int discountPercent() {
            if (!hasDiscount()) return 0;
            return (int) Math.round((oldPriceToman - priceToman) * 100.0 / oldPriceToman);
        }

        public String title() {
            if (isFa()) {
                switch (months) {
                    case 1: return "پرمیوم ۱ ماهه";
                    case 3: return "پرمیوم ۳ ماهه";
                    case 6: return "پرمیوم ۶ ماهه";
                    case 12: return "پرمیوم ۱ ساله";
                    default: return "پرمیوم " + faDigits(String.valueOf(months)) + " ماهه";
                }
            }
            return months == 12 ? "Premium 1 year" : "Premium " + months + (months == 1 ? " month" : " months");
        }

        public String priceText() {
            return money(priceToman);
        }

        public String oldPriceText() {
            return money(oldPriceToman);
        }

        public String perMonthText() {
            final long perMonth = Math.round(priceToman / (double) months);
            return isFa() ? "ماهانه " + money(perMonth) : money(perMonth) + " / month";
        }

        public String discountText() {
            final int p = discountPercent();
            return isFa() ? faDigits(String.valueOf(p)) + "٪ تخفیف" : p + "% off";
        }

        public String buttonText() {
            return isFa() ? "خرید " + title() + " · " + priceText() : "Buy " + title() + " · " + priceText();
        }
    }

    public static final PremiumPlan[] PREMIUM_PLANS = {
            new PremiumPlan("premium_1m", 1, 50000, 0),
            new PremiumPlan("premium_3m", 3, 75000, 0),
            new PremiumPlan("premium_6m", 6, 130000, 150000),
            new PremiumPlan("premium_12m", 12, 275000, 300000),
    };

    private static MyketBillingController instance;

    public static MyketBillingController getInstance() {
        if (instance == null) {
            instance = new MyketBillingController();
        }
        return instance;
    }

    private IabHelper helper;
    private boolean setupDone;
    private boolean setupRunning;
    private final ArrayList<Utilities.Callback2<Boolean, String>> setupWaiters = new ArrayList<>();
    private final Set<String> processingTokens = new HashSet<>();

    // ---------- helpers ----------

    private static boolean isFa() {
        try {
            LocaleController.LocaleInfo info = LocaleController.getInstance().getCurrentLocaleInfo();
            if (info != null && info.shortName != null) {
                return info.shortName.toLowerCase().startsWith("fa");
            }
            return "fa".equals(LocaleController.getInstance().getCurrentLocale().getLanguage());
        } catch (Exception e) {
            return false;
        }
    }

    private static String faDigits(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            sb.append(c >= '0' && c <= '9' ? (char) ('۰' + (c - '0')) : c);
        }
        return sb.toString();
    }

    private static String money(long toman) {
        final String n = String.format(Locale.US, "%,d", toman);
        return isFa() ? faDigits(n) + " تومان" : n + " Toman";
    }

    public static String skuForStars(long stars) {
        return "stars_" + stars;
    }

    private static long priceForStars(long stars) {
        for (int i = 0; i < STARS.length; ++i) {
            if (STARS[i] == stars) return PRICE_IRR[i];
        }
        return 0;
    }

    private static long starsForSku(String sku) {
        for (long s : STARS) {
            if (skuForStars(s).equals(sku)) return s;
        }
        return 0;
    }

    private static PremiumPlan planForSku(String sku) {
        for (PremiumPlan p : PREMIUM_PLANS) {
            if (p.sku.equals(sku)) return p;
        }
        return null;
    }

    private static List<String> allSkus() {
        ArrayList<String> list = new ArrayList<>();
        for (long s : STARS) list.add(skuForStars(s));
        for (PremiumPlan p : PREMIUM_PLANS) list.add(p.sku);
        return list;
    }

    // ---------- setup ----------

    private void ensureSetup(Utilities.Callback2<Boolean, String> callback) {
        if (setupDone && helper != null) {
            callback.run(true, null);
            return;
        }
        setupWaiters.add(callback);
        if (setupRunning) {
            return;
        }
        setupRunning = true;
        try {
            helper = new IabHelper(ApplicationLoader.applicationContext, BuildConfig.MYKET_PUBLIC_KEY);
            helper.enableDebugLogging(BuildVars.LOGS_ENABLED, TAG);
            helper.startSetup(result -> {
                setupRunning = false;
                final boolean ok = result.isSuccess();
                final String msg = ok ? null : "MYKET_SETUP: " + result.getMessage();
                setupDone = ok;
                if (!ok) {
                    try {
                        if (helper != null) helper.dispose();
                    } catch (Exception ignore) {}
                    helper = null;
                }
                ArrayList<Utilities.Callback2<Boolean, String>> waiters = new ArrayList<>(setupWaiters);
                setupWaiters.clear();
                for (Utilities.Callback2<Boolean, String> w : waiters) {
                    w.run(ok, msg);
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
            setupRunning = false;
            helper = null;
            ArrayList<Utilities.Callback2<Boolean, String>> waiters = new ArrayList<>(setupWaiters);
            setupWaiters.clear();
            for (Utilities.Callback2<Boolean, String> w : waiters) {
                w.run(false, "MYKET_SETUP_EXCEPTION");
            }
        }
    }

    // ---------- public API ----------

    /** Buy a Stars package. whenDone is always called on the UI thread; (false, null) = user cancelled. */
    public void buyStars(Activity activity, AccountInstance acc, long stars, Utilities.Callback2<Boolean, String> whenDone) {
        if (priceForStars(stars) == 0) {
            whenDone.run(false, "UNKNOWN_PACKAGE");
            return;
        }
        buySku(activity, acc, skuForStars(stars), whenDone);
    }

    /** Buy a Premium plan. whenDone is always called on the UI thread; (false, null) = user cancelled. */
    public void buyPremium(Activity activity, AccountInstance acc, PremiumPlan plan, Utilities.Callback2<Boolean, String> whenDone) {
        if (plan == null || planForSku(plan.sku) == null) {
            whenDone.run(false, "UNKNOWN_PACKAGE");
            return;
        }
        buySku(activity, acc, plan.sku, whenDone);
    }

    private void buySku(Activity activity, AccountInstance acc, String sku, Utilities.Callback2<Boolean, String> whenDone) {
        if (!acc.getUserConfig().isClientActivated()) {
            whenDone.run(false, "NOT_LOGGED_IN");
            return;
        }
        ensureSetup((ok, err) -> {
            if (!ok) {
                whenDone.run(false, err);
                return;
            }
            // Finish any purchase that was paid but not delivered, then start a new one.
            recoverPending(acc, () -> {
                final String payload = acc.getUserConfig().getClientUserId() + ":" + Utilities.random.nextLong();
                try {
                    helper.launchPurchaseFlow(activity, sku, (result, purchase) -> {
                        if (result.isFailure() || purchase == null) {
                            if (result.getResponse() == USER_CANCELLED) {
                                whenDone.run(false, null);
                            } else {
                                whenDone.run(false, "MYKET: " + result.getMessage());
                            }
                            return;
                        }
                        deliver(acc, purchase, whenDone);
                    }, payload);
                } catch (Exception e) {
                    FileLog.e(e);
                    whenDone.run(false, "MYKET_LAUNCH_ERROR");
                }
            });
        });
    }

    /** Re-send purchases that were paid but not consumed (crash, no network...). */
    public void recoverPending(AccountInstance acc, Runnable done) {
        if (helper == null || !setupDone) {
            if (done != null) done.run();
            return;
        }
        try {
            helper.queryInventoryAsync(false, allSkus(), (result, inventory) -> {
                if (result.isFailure() || inventory == null) {
                    if (done != null) done.run();
                    return;
                }
                final String userPrefix = acc.getUserConfig().getClientUserId() + ":";
                final ArrayList<Purchase> pending = new ArrayList<>();
                for (String sku : allSkus()) {
                    Purchase p = inventory.getPurchase(sku);
                    if (p != null && p.getPurchaseState() == 0
                            && p.getDeveloperPayload() != null
                            && p.getDeveloperPayload().startsWith(userPrefix)) {
                        pending.add(p);
                    }
                }
                deliverNext(acc, pending, 0, done);
            });
        } catch (Exception e) {
            FileLog.e(e);
            if (done != null) done.run();
        }
    }

    private void deliverNext(AccountInstance acc, ArrayList<Purchase> list, int index, Runnable done) {
        if (index >= list.size()) {
            if (done != null) done.run();
            return;
        }
        deliver(acc, list.get(index), (ok, err) -> deliverNext(acc, list, index + 1, done));
    }

    private void deliver(AccountInstance acc, Purchase purchase, Utilities.Callback2<Boolean, String> whenDone) {
        final String token = purchase.getToken();
        final long stars = starsForSku(purchase.getSku());
        final PremiumPlan plan = planForSku(purchase.getSku());
        if (stars == 0 && plan == null) {
            whenDone.run(false, "UNKNOWN_SKU");
            return;
        }
        final String userPrefix = acc.getUserConfig().getClientUserId() + ":";
        if (purchase.getDeveloperPayload() == null || !purchase.getDeveloperPayload().startsWith(userPrefix)) {
            FileLog.d(TAG + ": payload does not belong to current user, skipping");
            whenDone.run(false, "PAYLOAD_MISMATCH");
            return;
        }
        synchronized (processingTokens) {
            if (processingTokens.contains(token)) {
                whenDone.run(false, "ALREADY_PROCESSING");
                return;
            }
            processingTokens.add(token);
        }

        final TLRPC.TL_payments_assignPlayMarketTransaction req = new TLRPC.TL_payments_assignPlayMarketTransaction();
        req.receipt = new TLRPC.TL_dataJSON();
        try {
            JSONObject o = new JSONObject();
            o.put("store", "myket");
            o.put("sku", purchase.getSku());
            o.put("originalJson", purchase.getOriginalJson());
            o.put("signature", purchase.getSignature());
            req.receipt.data = o.toString();
        } catch (Exception e) {
            FileLog.e(e);
            synchronized (processingTokens) { processingTokens.remove(token); }
            whenDone.run(false, "RECEIPT_ERROR");
            return;
        }
        if (plan != null) {
            req.purpose = new TLRPC.TL_inputStorePaymentPremiumSubscription();
        } else {
            final TLRPC.TL_inputStorePaymentStarsTopup purpose = new TLRPC.TL_inputStorePaymentStarsTopup();
            purpose.stars = stars;
            purpose.currency = "IRR";
            purpose.amount = priceForStars(stars);
            req.purpose = purpose;
        }

        acc.getConnectionsManager().sendRequest(req, (response, error) -> AndroidUtilities.runOnUIThread(() -> {
            if (response instanceof TLRPC.Updates) {
                acc.getMessagesController().processUpdates((TLRPC.Updates) response, false);
                // Server accepted -> now consume so the user can buy again.
                try {
                    helper.consumeAsync(purchase, (p, r) -> {
                        synchronized (processingTokens) { processingTokens.remove(token); }
                        if (!r.isSuccess()) {
                            FileLog.d(TAG + ": consume failed: " + r.getMessage());
                        }
                        // Already credited by the server, so this is a success for the user.
                        whenDone.run(true, null);
                    });
                } catch (Exception e) {
                    FileLog.e(e);
                    synchronized (processingTokens) { processingTokens.remove(token); }
                    whenDone.run(true, null);
                }
            } else {
                synchronized (processingTokens) { processingTokens.remove(token); }
                String text = error != null ? error.text : "UNKNOWN_RESPONSE";
                FileLog.d(TAG + ": server rejected purchase: " + text);
                whenDone.run(false, text);
            }
        }), ConnectionsManager.RequestFlagFailOnServerErrorsExceptFloodWait | ConnectionsManager.RequestFlagInvokeAfter);
    }
}
