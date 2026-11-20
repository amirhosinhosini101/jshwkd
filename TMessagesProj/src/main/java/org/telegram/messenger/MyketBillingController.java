package org.telegram.messenger;

import android.app.Activity;

import org.json.JSONObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ir.myket.billingclient.IabHelper;
import ir.myket.billingclient.util.IabResult;
import ir.myket.billingclient.util.Purchase;

/**
 * Myket in-app billing for Gitbin Stars.
 * Flow: launchPurchaseFlow -> send receipt to our server -> server verifies & credits -> consume.
 * A purchase is consumed ONLY after the server accepted it. Unconsumed purchases are
 * re-sent to the server on the next buy (recoverPending), so the server must be idempotent
 * per purchaseToken.
 */
public class MyketBillingController {

    private static final String TAG = "MyketBilling";
    private static final int USER_CANCELLED = -1005;

    // SKU -> stars / price in Rial (price is informational, server must use its own table)
    private static final long[] STARS = {10, 50, 100, 500, 1000};
    private static final long[] PRICE_IRR = {50000, 250000, 500000, 2500000, 5000000};

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

    public static String skuForStars(long stars) {
        return "stars_" + stars;
    }

    private static long priceForStars(long stars) {
        for (int i = 0; i < STARS.length; ++i) {
            if (STARS[i] == stars) return PRICE_IRR[i];
        }
        return 0;
    }

    private static List<String> allSkus() {
        ArrayList<String> list = new ArrayList<>();
        for (long s : STARS) list.add(skuForStars(s));
        return list;
    }

    private static long starsForSku(String sku) {
        for (long s : STARS) {
            if (skuForStars(s).equals(sku)) return s;
        }
        return 0;
    }

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

    /** Buy a Stars package. whenDone is always called on the UI thread. */
    public void buyStars(Activity activity, AccountInstance acc, long stars, Utilities.Callback2<Boolean, String> whenDone) {
        final String sku = skuForStars(stars);
        if (priceForStars(stars) == 0) {
            whenDone.run(false, "UNKNOWN_PACKAGE");
            return;
        }
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
        if (stars == 0) {
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
        final TLRPC.TL_inputStorePaymentStarsTopup purpose = new TLRPC.TL_inputStorePaymentStarsTopup();
        purpose.stars = stars;
        purpose.currency = "IRR";
        purpose.amount = priceForStars(stars);
        req.purpose = purpose;

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
                        // Stars are already credited by the server, so this is a success for the user.
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
