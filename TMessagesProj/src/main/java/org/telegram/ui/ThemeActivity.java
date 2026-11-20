package org.telegram.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.webkit.Profile;
import com.google.firebase.appindexing.builders.TimerBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.time.SunDate;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_account;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.EmojiThemes;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.AppIconsSelectorCell;
import org.telegram.ui.Cells.BrightnessControlCell;
import org.telegram.ui.Cells.ChatListCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.RadioButtonCell;
import org.telegram.ui.Cells.RadioColorCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.ThemePreviewMessagesCell;
import org.telegram.ui.Cells.ThemeTypeCell;
import org.telegram.ui.Cells.ThemesHorizontalListCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PermissionRequest;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SeekBarView;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.SimpleThemeDescription;
import org.telegram.ui.Components.SwipeGestureSettingsView;
import org.telegram.ui.Components.TextHelper;
import org.telegram.ui.PeerColorActivity;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;
import org.telegram.ui.bots.BotWebViewSheet;
import org.telegram.ui.bots.WebViewRequestProps;
import org.telegram.ui.web.SearchEngine;
import org.telegram.ui.web.WebBrowserSettings;

/* JADX INFO: loaded from: classes9.dex */
public class ThemeActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    public static final int THEME_TYPE_BASIC = 0;
    public static final int THEME_TYPE_NIGHT = 1;
    public static final int THEME_TYPE_OTHER = 2;
    public static final int THEME_TYPE_THEMES_BROWSER = 3;
    private static final int create_theme = 1;
    private static final int day_night_switch = 5;
    private static final int edit_theme = 3;
    private static final int reset_settings = 4;
    private static final int share_theme = 2;
    private int appIconHeaderRow;
    private int appIconSelectorRow;
    private int appIconShadowRow;
    private int automaticBrightnessInfoRow;
    private int automaticBrightnessRow;
    private int automaticHeaderRow;
    private int backgroundRow;
    private int bluetoothScoRow;
    private int browserRow;
    private int bubbleRadiusHeaderRow;
    private int bubbleRadiusInfoRow;
    private int bubbleRadiusRow;
    private int changeUserColor;
    private int chatBlurRow;
    private int chatListHeaderRow;
    private int chatListInfoRow;
    private int chatListRow;
    private int contactsReimportRow;
    private int contactsSortRow;
    private int createNewThemeRow;
    private int currentType;
    private ArrayList<Theme.ThemeInfo> darkThemes = new ArrayList<>();
    private ArrayList<Theme.ThemeInfo> defaultThemes = new ArrayList<>();
    private int directShareRow;
    private int distanceRow;
    private int editThemeRow;
    private int enableAnimationsRow;
    private GpsLocationListener gpsLocationListener;
    boolean hasThemeAccents;
    private boolean highlightSensitiveRow;
    boolean lastIsDarkTheme;
    private int lastShadowRow;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int liteModeInfoRow;
    private int liteModeRow;
    private int mediaSoundHeaderRow;
    private int mediaSoundSectionRow;
    private ActionBarMenuItem menuItem;
    private GpsLocationListener networkLocationListener;
    private int newThemeInfoRow;
    private int nextMediaTapRow;
    private int nightAutomaticRow;
    private int nightDisabledRow;
    private int nightScheduledRow;
    private int nightSystemDefaultRow;
    private int nightThemeRow;
    private int nightTypeInfoRow;
    private int otherHeaderRow;
    private int otherSectionRow;
    private int pauseOnMediaRow;
    private int pauseOnRecordRow;
    private int preferedHeaderRow;
    private boolean previousByLocation;
    private int previousUpdatedType;
    private int raiseToListenRow;
    private int raiseToSpeakRow;
    private int rowCount;
    private int saveToGalleryOption1Row;
    private int saveToGalleryOption2Row;
    private int saveToGallerySectionRow;
    private int scheduleFromRow;
    private int scheduleFromToInfoRow;
    private int scheduleHeaderRow;
    private int scheduleLocationInfoRow;
    private int scheduleLocationRow;
    private int scheduleToRow;
    private int scheduleUpdateLocationRow;
    private int searchEngineRow;
    private int selectThemeHeaderRow;
    private int sendByEnterRow;
    private int sensitiveContentRow;
    private int settings2Row;
    private int settingsRow;
    private Theme.ThemeAccent sharingAccent;
    private AlertDialog sharingProgressDialog;
    private Theme.ThemeInfo sharingTheme;
    private int stickersInfoRow;
    private int stickersRow;
    private int stickersSectionRow;
    private RLottieDrawable sunDrawable;
    private int swipeGestureHeaderRow;
    private int swipeGestureInfoRow;
    private int swipeGestureRow;
    private int textSizeHeaderRow;
    private int textSizeRow;
    private int themeAccentListRow;
    private int themeHeaderRow;
    private int themeInfoRow;
    private int themeListRow;
    private int themeListRow2;
    private int themePreviewRow;
    private ThemesHorizontalListCell themesHorizontalListCell;
    private boolean updateDistance;
    private boolean updateRecordViaSco;
    private boolean updateSearchEngine;
    private boolean updatingLocation;

    public interface SizeChooseViewDelegate {
        void onSizeChanged();
    }

    public ThemeActivity highlightSensitiveRow() {
        this.highlightSensitiveRow = true;
        return this;
    }

    private class GpsLocationListener implements LocationListener {
        private GpsLocationListener() {
        }

        /* synthetic */ GpsLocationListener(ThemeActivity x0, AnonymousClass1 x1) {
            this();
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (location != null) {
                ThemeActivity.this.stopLocationUpdate();
                ThemeActivity.this.updateSunTime(location, false);
            }
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String provider) {
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String provider) {
        }
    }

    private class TextSizeCell extends FrameLayout {
        private int endFontSize;
        private int lastWidth;
        private ThemePreviewMessagesCell messagesCell;
        private SeekBarView sizeBar;
        private int startFontSize;
        private TextPaint textPaint;

        public TextSizeCell(Context context) {
            super(context);
            this.startFontSize = 12;
            this.endFontSize = 30;
            setWillNotDraw(false);
            this.textPaint = new TextPaint(1);
            this.textPaint.setTextSize(AndroidUtilities.dp(16.0f));
            this.sizeBar = new SeekBarView(context);
            this.sizeBar.setReportChanges(true);
            this.sizeBar.setSeparatorsCount((this.endFontSize - this.startFontSize) + 1);
            this.sizeBar.setDelegate(new SeekBarView.SeekBarViewDelegate() { // from class: org.telegram.ui.ThemeActivity.TextSizeCell.1

                @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                public void onSeekBarDrag(boolean stop, float progress) {
                    ThemeActivity.this.setFontSize(Math.round(TextSizeCell.this.startFontSize + ((TextSizeCell.this.endFontSize - TextSizeCell.this.startFontSize) * progress)));
                }

                @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                public void onSeekBarPressed(boolean pressed) {
                }

                @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                public CharSequence getContentDescription() {
                    return String.valueOf(Math.round(TextSizeCell.this.startFontSize + ((TextSizeCell.this.endFontSize - TextSizeCell.this.startFontSize) * TextSizeCell.this.sizeBar.getProgress())));
                }

                @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                public int getStepsCount() {
                    return TextSizeCell.this.endFontSize - TextSizeCell.this.startFontSize;
                }
            });
            this.sizeBar.setImportantForAccessibility(2);
            addView(this.sizeBar, LayoutHelper.createFrame(-1, 38.0f, 51, 5.0f, 5.0f, 39.0f, 0.0f));
            this.messagesCell = new ThemePreviewMessagesCell(context, ThemeActivity.this.parentLayout, 0);
            this.messagesCell.setImportantForAccessibility(4);
            addView(this.messagesCell, LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, 53.0f, 0.0f, 0.0f));
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            this.textPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteValueText));
            canvas.drawText("" + SharedConfig.fontSize, getMeasuredWidth() - AndroidUtilities.dp(39.0f), AndroidUtilities.dp(28.0f), this.textPaint);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int width = View.MeasureSpec.getSize(widthMeasureSpec);
            if (this.lastWidth != width) {
                this.sizeBar.setProgress((SharedConfig.fontSize - this.startFontSize) / (this.endFontSize - this.startFontSize));
                this.lastWidth = width;
            }
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            this.messagesCell.invalidate();
            this.sizeBar.invalidate();
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            this.sizeBar.getSeekBarAccessibilityDelegate().onInitializeAccessibilityNodeInfoInternal(this, info);
        }

        @Override // android.view.View
        public boolean performAccessibilityAction(int action, Bundle arguments) {
            return super.performAccessibilityAction(action, arguments) || this.sizeBar.getSeekBarAccessibilityDelegate().performAccessibilityActionInternal(this, action, arguments);
        }
    }

    private class BubbleRadiusCell extends FrameLayout {
        private int endRadius;
        private SeekBarView sizeBar;
        private int startRadius;
        private TextPaint textPaint;

        public BubbleRadiusCell(Context context) {
            super(context);
            this.startRadius = 0;
            this.endRadius = 17;
            setWillNotDraw(false);
            this.textPaint = new TextPaint(1);
            this.textPaint.setTextSize(AndroidUtilities.dp(16.0f));
            this.sizeBar = new SeekBarView(context);
            this.sizeBar.setReportChanges(true);
            this.sizeBar.setSeparatorsCount((this.endRadius - this.startRadius) + 1);
            this.sizeBar.setDelegate(new SeekBarView.SeekBarViewDelegate() { // from class: org.telegram.ui.ThemeActivity.BubbleRadiusCell.1

                @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                public void onSeekBarDrag(boolean stop, float progress) {
                    ThemeActivity.this.setBubbleRadius(Math.round(BubbleRadiusCell.this.startRadius + ((BubbleRadiusCell.this.endRadius - BubbleRadiusCell.this.startRadius) * progress)), false);
                }

                @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                public void onSeekBarPressed(boolean pressed) {
                }

                @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                public CharSequence getContentDescription() {
                    return String.valueOf(Math.round(BubbleRadiusCell.this.startRadius + ((BubbleRadiusCell.this.endRadius - BubbleRadiusCell.this.startRadius) * BubbleRadiusCell.this.sizeBar.getProgress())));
                }

                @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                public int getStepsCount() {
                    return BubbleRadiusCell.this.endRadius - BubbleRadiusCell.this.startRadius;
                }
            });
            this.sizeBar.setImportantForAccessibility(2);
            addView(this.sizeBar, LayoutHelper.createFrame(-1, 38.0f, 51, 5.0f, 5.0f, 39.0f, 0.0f));
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            this.textPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteValueText));
            canvas.drawText("" + SharedConfig.bubbleRadius, getMeasuredWidth() - AndroidUtilities.dp(39.0f), AndroidUtilities.dp(28.0f), this.textPaint);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec), 1073741824), heightMeasureSpec);
            this.sizeBar.setProgress((SharedConfig.bubbleRadius - this.startRadius) / (this.endRadius - this.startRadius));
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            this.sizeBar.invalidate();
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            this.sizeBar.getSeekBarAccessibilityDelegate().onInitializeAccessibilityNodeInfoInternal(this, info);
        }

        @Override // android.view.View
        public boolean performAccessibilityAction(int action, Bundle arguments) {
            return super.performAccessibilityAction(action, arguments) || this.sizeBar.getSeekBarAccessibilityDelegate().performAccessibilityActionInternal(this, action, arguments);
        }
    }

    public ThemeActivity(int type) {
        AnonymousClass1 anonymousClass1 = null;
        this.gpsLocationListener = new GpsLocationListener(this, anonymousClass1);
        this.networkLocationListener = new GpsLocationListener(this, anonymousClass1);
        this.currentType = type;
        updateRows(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setBubbleRadius(int size, boolean layout) {
        if (size != SharedConfig.bubbleRadius) {
            SharedConfig.bubbleRadius = size;
            SharedPreferences preferences = MessagesController.getGlobalMainSettings();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("bubbleRadius", SharedConfig.bubbleRadius);
            editor.commit();
            RecyclerView.ViewHolder holder = this.listView.findViewHolderForAdapterPosition(this.textSizeRow);
            if (holder != null && (holder.itemView instanceof TextSizeCell)) {
                TextSizeCell cell = (TextSizeCell) holder.itemView;
                ChatMessageCell[] cells = cell.messagesCell.getCells();
                for (int a = 0; a < cells.length; a++) {
                    cells[a].getMessageObject().resetLayout();
                    cells[a].requestLayout();
                }
                cell.invalidate();
            }
            RecyclerView.ViewHolder holder2 = this.listView.findViewHolderForAdapterPosition(this.bubbleRadiusRow);
            if (holder2 != null && (holder2.itemView instanceof BubbleRadiusCell)) {
                BubbleRadiusCell cell2 = (BubbleRadiusCell) holder2.itemView;
                if (layout) {
                    cell2.requestLayout();
                } else {
                    cell2.invalidate();
                }
            }
            updateMenuItem();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setFontSize(int size) {
        if (size == SharedConfig.fontSize) {
            return false;
        }
        SharedConfig.fontSize = size;
        SharedConfig.fontSizeIsDefault = false;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
        if (preferences == null) {
            return false;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("fons_size", SharedConfig.fontSize);
        editor.commit();
        Theme.createCommonMessageResources();
        RecyclerView.ViewHolder holder = this.listView.findViewHolderForAdapterPosition(this.textSizeRow);
        if (holder != null && (holder.itemView instanceof TextSizeCell)) {
            TextSizeCell cell = (TextSizeCell) holder.itemView;
            ChatMessageCell[] cells = cell.messagesCell.getCells();
            for (int a = 0; a < cells.length; a++) {
                cells[a].getMessageObject().resetLayout();
                cells[a].requestLayout();
            }
        }
        updateMenuItem();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRows(boolean notify) {
        int oldRowCount = this.rowCount;
        int prevThemeAccentListRow = this.themeAccentListRow;
        int prevEditThemeRow = this.editThemeRow;
        int prevRaiseToSpeakRow = this.raiseToSpeakRow;
        this.rowCount = 0;
        this.contactsReimportRow = -1;
        this.contactsSortRow = -1;
        this.scheduleLocationRow = -1;
        this.scheduleUpdateLocationRow = -1;
        this.scheduleLocationInfoRow = -1;
        this.nightDisabledRow = -1;
        this.nightScheduledRow = -1;
        this.nightAutomaticRow = -1;
        this.nightSystemDefaultRow = -1;
        this.nightTypeInfoRow = -1;
        this.scheduleHeaderRow = -1;
        this.nightThemeRow = -1;
        this.browserRow = -1;
        this.newThemeInfoRow = -1;
        this.scheduleFromRow = -1;
        this.scheduleToRow = -1;
        this.scheduleFromToInfoRow = -1;
        this.themeListRow = -1;
        this.themeListRow2 = -1;
        this.themeAccentListRow = -1;
        this.themeInfoRow = -1;
        this.preferedHeaderRow = -1;
        this.automaticHeaderRow = -1;
        this.automaticBrightnessRow = -1;
        this.automaticBrightnessInfoRow = -1;
        this.textSizeHeaderRow = -1;
        this.themeHeaderRow = -1;
        this.bubbleRadiusHeaderRow = -1;
        this.bubbleRadiusRow = -1;
        this.bubbleRadiusInfoRow = -1;
        this.chatListHeaderRow = -1;
        this.chatListRow = -1;
        this.chatListInfoRow = -1;
        this.chatBlurRow = -1;
        this.pauseOnRecordRow = -1;
        this.pauseOnMediaRow = -1;
        this.stickersRow = -1;
        this.stickersInfoRow = -1;
        this.stickersSectionRow = -1;
        this.mediaSoundHeaderRow = -1;
        this.otherHeaderRow = -1;
        this.mediaSoundSectionRow = -1;
        this.otherSectionRow = -1;
        this.liteModeRow = -1;
        this.liteModeInfoRow = -1;
        this.textSizeRow = -1;
        this.backgroundRow = -1;
        this.changeUserColor = -1;
        this.settingsRow = -1;
        this.directShareRow = -1;
        this.sensitiveContentRow = -1;
        this.enableAnimationsRow = -1;
        this.raiseToSpeakRow = -1;
        this.raiseToListenRow = -1;
        this.nextMediaTapRow = -1;
        this.sendByEnterRow = -1;
        this.saveToGalleryOption1Row = -1;
        this.saveToGalleryOption2Row = -1;
        this.saveToGallerySectionRow = -1;
        this.distanceRow = -1;
        this.searchEngineRow = -1;
        this.bluetoothScoRow = -1;
        this.settings2Row = -1;
        this.swipeGestureHeaderRow = -1;
        this.swipeGestureRow = -1;
        this.swipeGestureInfoRow = -1;
        this.selectThemeHeaderRow = -1;
        this.themePreviewRow = -1;
        this.editThemeRow = -1;
        this.createNewThemeRow = -1;
        this.appIconHeaderRow = -1;
        this.appIconSelectorRow = -1;
        this.appIconShadowRow = -1;
        this.lastShadowRow = -1;
        this.defaultThemes.clear();
        this.darkThemes.clear();
        int a = 0;
        int N = Theme.themes.size();
        while (true) {
            if (a >= N) {
                break;
            }
            Theme.ThemeInfo themeInfo = Theme.themes.get(a);
            if (this.currentType == 0 || this.currentType == 3 || (!themeInfo.isLight() && (themeInfo.info == null || themeInfo.info.document != null))) {
                if (themeInfo.pathToFile != null) {
                    this.darkThemes.add(themeInfo);
                } else {
                    this.defaultThemes.add(themeInfo);
                }
            }
            a++;
        }
        Collections.sort(this.defaultThemes, new Comparator() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda18
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return Integer.compare(((Theme.ThemeInfo) obj).sortIndex, ((Theme.ThemeInfo) obj2).sortIndex);
            }
        });
        if (this.currentType == 3) {
            int i = this.rowCount;
            this.rowCount = i + 1;
            this.selectThemeHeaderRow = i;
            int i2 = this.rowCount;
            this.rowCount = i2 + 1;
            this.themeListRow2 = i2;
            int i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.chatListInfoRow = i3;
            int i4 = this.rowCount;
            this.rowCount = i4 + 1;
            this.themePreviewRow = i4;
            int i5 = this.rowCount;
            this.rowCount = i5 + 1;
            this.themeHeaderRow = i5;
            int i6 = this.rowCount;
            this.rowCount = i6 + 1;
            this.themeListRow = i6;
            this.hasThemeAccents = Theme.getCurrentTheme().hasAccentColors();
            if (this.themesHorizontalListCell != null) {
                this.themesHorizontalListCell.setDrawDivider(this.hasThemeAccents);
            }
            if (this.hasThemeAccents) {
                int i7 = this.rowCount;
                this.rowCount = i7 + 1;
                this.themeAccentListRow = i7;
            }
            int i8 = this.rowCount;
            this.rowCount = i8 + 1;
            this.bubbleRadiusInfoRow = i8;
            Theme.ThemeInfo themeInfo2 = Theme.getCurrentTheme();
            Theme.ThemeAccent accent = themeInfo2.getAccent(false);
            if (themeInfo2.themeAccents != null && !themeInfo2.themeAccents.isEmpty() && accent != null && accent.id >= 100) {
                int i9 = this.rowCount;
                this.rowCount = i9 + 1;
                this.editThemeRow = i9;
            }
            int i10 = this.rowCount;
            this.rowCount = i10 + 1;
            this.createNewThemeRow = i10;
            int i11 = this.rowCount;
            this.rowCount = i11 + 1;
            this.lastShadowRow = i11;
        } else if (this.currentType == 0) {
            int i12 = this.rowCount;
            this.rowCount = i12 + 1;
            this.textSizeHeaderRow = i12;
            int i13 = this.rowCount;
            this.rowCount = i13 + 1;
            this.textSizeRow = i13;
            int i14 = this.rowCount;
            this.rowCount = i14 + 1;
            this.backgroundRow = i14;
            int i15 = this.rowCount;
            this.rowCount = i15 + 1;
            this.changeUserColor = i15;
            int i16 = this.rowCount;
            this.rowCount = i16 + 1;
            this.newThemeInfoRow = i16;
            int i17 = this.rowCount;
            this.rowCount = i17 + 1;
            this.themeHeaderRow = i17;
            int i18 = this.rowCount;
            this.rowCount = i18 + 1;
            this.themeListRow2 = i18;
            int i19 = this.rowCount;
            this.rowCount = i19 + 1;
            this.themeInfoRow = i19;
            int i20 = this.rowCount;
            this.rowCount = i20 + 1;
            this.bubbleRadiusHeaderRow = i20;
            int i21 = this.rowCount;
            this.rowCount = i21 + 1;
            this.bubbleRadiusRow = i21;
            int i22 = this.rowCount;
            this.rowCount = i22 + 1;
            this.bubbleRadiusInfoRow = i22;
            int i23 = this.rowCount;
            this.rowCount = i23 + 1;
            this.chatListHeaderRow = i23;
            int i24 = this.rowCount;
            this.rowCount = i24 + 1;
            this.chatListRow = i24;
            int i25 = this.rowCount;
            this.rowCount = i25 + 1;
            this.chatListInfoRow = i25;
            int i26 = this.rowCount;
            this.rowCount = i26 + 1;
            this.swipeGestureHeaderRow = i26;
            int i27 = this.rowCount;
            this.rowCount = i27 + 1;
            this.swipeGestureRow = i27;
            int i28 = this.rowCount;
            this.rowCount = i28 + 1;
            this.swipeGestureInfoRow = i28;
            int i29 = this.rowCount;
            this.rowCount = i29 + 1;
            this.nightThemeRow = i29;
            int i30 = this.rowCount;
            this.rowCount = i30 + 1;
            this.browserRow = i30;
            int i31 = this.rowCount;
            this.rowCount = i31 + 1;
            this.liteModeRow = i31;
            int i32 = this.rowCount;
            this.rowCount = i32 + 1;
            this.stickersRow = i32;
            int i33 = this.rowCount;
            this.rowCount = i33 + 1;
            this.stickersSectionRow = i33;
            int i34 = this.rowCount;
            this.rowCount = i34 + 1;
            this.mediaSoundHeaderRow = i34;
            int i35 = this.rowCount;
            this.rowCount = i35 + 1;
            this.nextMediaTapRow = i35;
            int i36 = this.rowCount;
            this.rowCount = i36 + 1;
            this.raiseToListenRow = i36;
            if (SharedConfig.raiseToListen) {
                int i37 = this.rowCount;
                this.rowCount = i37 + 1;
                this.raiseToSpeakRow = i37;
            }
            int i38 = this.rowCount;
            this.rowCount = i38 + 1;
            this.pauseOnRecordRow = i38;
            int i39 = this.rowCount;
            this.rowCount = i39 + 1;
            this.pauseOnMediaRow = i39;
            int i40 = this.rowCount;
            this.rowCount = i40 + 1;
            this.bluetoothScoRow = i40;
            int i41 = this.rowCount;
            this.rowCount = i41 + 1;
            this.mediaSoundSectionRow = i41;
            int i42 = this.rowCount;
            this.rowCount = i42 + 1;
            this.otherHeaderRow = i42;
            int i43 = this.rowCount;
            this.rowCount = i43 + 1;
            this.directShareRow = i43;
            int i44 = this.rowCount;
            this.rowCount = i44 + 1;
            this.sendByEnterRow = i44;
            int i45 = this.rowCount;
            this.rowCount = i45 + 1;
            this.distanceRow = i45;
            int i46 = this.rowCount;
            this.rowCount = i46 + 1;
            this.otherSectionRow = i46;
        } else {
            int i47 = this.rowCount;
            this.rowCount = i47 + 1;
            this.nightDisabledRow = i47;
            int i48 = this.rowCount;
            this.rowCount = i48 + 1;
            this.nightScheduledRow = i48;
            int i49 = this.rowCount;
            this.rowCount = i49 + 1;
            this.nightAutomaticRow = i49;
            if (Build.VERSION.SDK_INT >= 29) {
                int i50 = this.rowCount;
                this.rowCount = i50 + 1;
                this.nightSystemDefaultRow = i50;
            }
            int i51 = this.rowCount;
            this.rowCount = i51 + 1;
            this.nightTypeInfoRow = i51;
            if (Theme.selectedAutoNightType == 1) {
                int i52 = this.rowCount;
                this.rowCount = i52 + 1;
                this.scheduleHeaderRow = i52;
                int i53 = this.rowCount;
                this.rowCount = i53 + 1;
                this.scheduleLocationRow = i53;
                if (Theme.autoNightScheduleByLocation) {
                    int i54 = this.rowCount;
                    this.rowCount = i54 + 1;
                    this.scheduleUpdateLocationRow = i54;
                    int i55 = this.rowCount;
                    this.rowCount = i55 + 1;
                    this.scheduleLocationInfoRow = i55;
                } else {
                    int i56 = this.rowCount;
                    this.rowCount = i56 + 1;
                    this.scheduleFromRow = i56;
                    int i57 = this.rowCount;
                    this.rowCount = i57 + 1;
                    this.scheduleToRow = i57;
                    int i58 = this.rowCount;
                    this.rowCount = i58 + 1;
                    this.scheduleFromToInfoRow = i58;
                }
            } else if (Theme.selectedAutoNightType == 2) {
                int i59 = this.rowCount;
                this.rowCount = i59 + 1;
                this.automaticHeaderRow = i59;
                int i60 = this.rowCount;
                this.rowCount = i60 + 1;
                this.automaticBrightnessRow = i60;
                int i61 = this.rowCount;
                this.rowCount = i61 + 1;
                this.automaticBrightnessInfoRow = i61;
            }
            if (Theme.selectedAutoNightType != 0) {
                int i62 = this.rowCount;
                this.rowCount = i62 + 1;
                this.preferedHeaderRow = i62;
                int i63 = this.rowCount;
                this.rowCount = i63 + 1;
                this.themeListRow = i63;
                this.hasThemeAccents = Theme.getCurrentNightTheme().hasAccentColors();
                if (this.themesHorizontalListCell != null) {
                    this.themesHorizontalListCell.setDrawDivider(this.hasThemeAccents);
                }
                if (this.hasThemeAccents) {
                    int i64 = this.rowCount;
                    this.rowCount = i64 + 1;
                    this.themeAccentListRow = i64;
                }
                int i65 = this.rowCount;
                this.rowCount = i65 + 1;
                this.themeInfoRow = i65;
            }
        }
        if (this.themesHorizontalListCell != null) {
            this.themesHorizontalListCell.notifyDataSetChanged(this.listView.getWidth());
        }
        if (this.listAdapter != null) {
            if (this.currentType != 1 || this.previousUpdatedType == Theme.selectedAutoNightType || this.previousUpdatedType == -1) {
                if (notify || this.previousUpdatedType == -1) {
                    this.listAdapter.notifyDataSetChanged();
                } else {
                    if (prevThemeAccentListRow == -1 && this.themeAccentListRow != -1) {
                        this.listAdapter.notifyItemInserted(this.themeAccentListRow);
                    } else if (prevThemeAccentListRow == -1 || this.themeAccentListRow != -1) {
                        if (this.themeAccentListRow != -1) {
                            this.listAdapter.notifyItemChanged(this.themeAccentListRow);
                        }
                    } else {
                        this.listAdapter.notifyItemRemoved(prevThemeAccentListRow);
                        if (prevEditThemeRow != -1) {
                            prevEditThemeRow--;
                        }
                    }
                    if (prevEditThemeRow == -1 && this.editThemeRow != -1) {
                        this.listAdapter.notifyItemInserted(this.editThemeRow);
                    } else if (prevEditThemeRow != -1 && this.editThemeRow == -1) {
                        this.listAdapter.notifyItemRemoved(prevEditThemeRow);
                    }
                    if (prevRaiseToSpeakRow == -1 && this.raiseToSpeakRow != -1) {
                        this.listAdapter.notifyItemInserted(this.raiseToSpeakRow);
                    } else if (prevRaiseToSpeakRow != -1 && this.raiseToSpeakRow == -1) {
                        this.listAdapter.notifyItemRemoved(prevRaiseToSpeakRow);
                    }
                }
            } else {
                int start = this.nightTypeInfoRow + 1;
                if (this.previousUpdatedType != Theme.selectedAutoNightType) {
                    int a2 = 0;
                    while (true) {
                        if (a2 >= 4) {
                            break;
                        }
                        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findViewHolderForAdapterPosition(a2);
                        if (holder != null && (holder.itemView instanceof ThemeTypeCell)) {
                            ((ThemeTypeCell) holder.itemView).setTypeChecked(a2 == Theme.selectedAutoNightType);
                        }
                        a2++;
                    }
                    if (Theme.selectedAutoNightType == 0) {
                        this.listAdapter.notifyItemRangeRemoved(start, oldRowCount - start);
                    } else if (Theme.selectedAutoNightType == 1) {
                        if (this.previousUpdatedType == 0) {
                            this.listAdapter.notifyItemRangeInserted(start, this.rowCount - start);
                        } else if (this.previousUpdatedType == 2) {
                            this.listAdapter.notifyItemRangeRemoved(start, 3);
                            this.listAdapter.notifyItemRangeInserted(start, Theme.autoNightScheduleByLocation ? 4 : 5);
                        } else if (this.previousUpdatedType == 3) {
                            this.listAdapter.notifyItemRangeInserted(start, Theme.autoNightScheduleByLocation ? 4 : 5);
                        }
                    } else if (Theme.selectedAutoNightType == 2) {
                        if (this.previousUpdatedType == 0) {
                            this.listAdapter.notifyItemRangeInserted(start, this.rowCount - start);
                        } else if (this.previousUpdatedType == 1) {
                            this.listAdapter.notifyItemRangeRemoved(start, Theme.autoNightScheduleByLocation ? 4 : 5);
                            this.listAdapter.notifyItemRangeInserted(start, 3);
                        } else if (this.previousUpdatedType == 3) {
                            this.listAdapter.notifyItemRangeInserted(start, 3);
                        }
                    } else if (Theme.selectedAutoNightType == 3) {
                        if (this.previousUpdatedType == 0) {
                            this.listAdapter.notifyItemRangeInserted(start, this.rowCount - start);
                        } else if (this.previousUpdatedType == 2) {
                            this.listAdapter.notifyItemRangeRemoved(start, 3);
                        } else if (this.previousUpdatedType == 1) {
                            this.listAdapter.notifyItemRangeRemoved(start, Theme.autoNightScheduleByLocation ? 4 : 5);
                        }
                    }
                } else if (this.previousByLocation != Theme.autoNightScheduleByLocation) {
                    this.listAdapter.notifyItemRangeRemoved(start + 2, Theme.autoNightScheduleByLocation ? 3 : 2);
                    this.listAdapter.notifyItemRangeInserted(start + 2, Theme.autoNightScheduleByLocation ? 2 : 3);
                }
            }
        }
        if (this.currentType == 1) {
            this.previousByLocation = Theme.autoNightScheduleByLocation;
            this.previousUpdatedType = Theme.selectedAutoNightType;
        }
        updateMenuItem();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.locationPermissionGranted);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.themeListUpdated);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.themeAccentListUpdated);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needShareTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needSetDayNightTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiPreviewThemesChanged);
        getNotificationCenter().addObserver(this, NotificationCenter.appConfigUpdated);
        getNotificationCenter().addObserver(this, NotificationCenter.contentSettingsLoaded);
        getNotificationCenter().addObserver(this, NotificationCenter.themeUploadedToServer);
        getNotificationCenter().addObserver(this, NotificationCenter.themeUploadError);
        getNotificationCenter().addObserver(this, NotificationCenter.webBrowserSettingsUpdate);
        if (this.currentType == 0) {
            Theme.loadRemoteThemes(this.currentAccount, true);
            Theme.checkCurrentRemoteTheme(true);
        }
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        stopLocationUpdate();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.locationPermissionGranted);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.themeListUpdated);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.themeAccentListUpdated);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needShareTheme);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needSetDayNightTheme);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiPreviewThemesChanged);
        getNotificationCenter().removeObserver(this, NotificationCenter.appConfigUpdated);
        getNotificationCenter().removeObserver(this, NotificationCenter.contentSettingsLoaded);
        getNotificationCenter().removeObserver(this, NotificationCenter.themeUploadedToServer);
        getNotificationCenter().removeObserver(this, NotificationCenter.themeUploadError);
        getNotificationCenter().removeObserver(this, NotificationCenter.webBrowserSettingsUpdate);
        Theme.saveAutoNightThemeConfig();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.locationPermissionGranted) {
            updateSunTime(null, true);
            return;
        }
        if (id == NotificationCenter.didSetNewWallpapper || id == NotificationCenter.emojiLoaded) {
            if (this.listView != null) {
                this.listView.invalidateViews();
            }
            updateMenuItem();
            return;
        }
        if (id == NotificationCenter.webBrowserSettingsUpdate) {
            if (this.listAdapter != null && this.browserRow != -1) {
                this.listAdapter.notifyItemChanged(this.browserRow);
                return;
            }
            return;
        }
        if (id == NotificationCenter.themeAccentListUpdated) {
            if (this.listAdapter != null && this.themeAccentListRow != -1) {
                this.listAdapter.notifyItemChanged(this.themeAccentListRow, new Object());
                return;
            }
            return;
        }
        if (id == NotificationCenter.themeListUpdated) {
            updateRows(true);
            return;
        }
        if (id == NotificationCenter.themeUploadedToServer) {
            Theme.ThemeInfo themeInfo = (Theme.ThemeInfo) args[0];
            Theme.ThemeAccent accent = (Theme.ThemeAccent) args[1];
            if (themeInfo == this.sharingTheme && accent == this.sharingAccent) {
                String link = "https://" + getMessagesController().linkPrefix + "/addtheme/" + (accent != null ? accent.info : themeInfo.info).slug;
                showDialog(new ShareAlert(getParentActivity(), null, link, false, link, false));
                if (this.sharingProgressDialog != null) {
                    this.sharingProgressDialog.dismiss();
                    return;
                }
                return;
            }
            return;
        }
        if (id == NotificationCenter.themeUploadError) {
            Theme.ThemeInfo themeInfo2 = (Theme.ThemeInfo) args[0];
            Theme.ThemeAccent accent2 = (Theme.ThemeAccent) args[1];
            if (themeInfo2 == this.sharingTheme && accent2 == this.sharingAccent && this.sharingProgressDialog == null) {
                this.sharingProgressDialog.dismiss();
                return;
            }
            return;
        }
        if (id == NotificationCenter.needShareTheme) {
            if (getParentActivity() == null || this.isPaused) {
                return;
            }
            this.sharingTheme = (Theme.ThemeInfo) args[0];
            this.sharingAccent = (Theme.ThemeAccent) args[1];
            this.sharingProgressDialog = new AlertDialog(getParentActivity(), 3);
            this.sharingProgressDialog.setCanCancel(true);
            showDialog(this.sharingProgressDialog, new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda6
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    m12308lambda$didReceivedNotification$1$orgtelegramuiThemeActivity(dialogInterface);
                }
            });
            return;
        }
        if (id == NotificationCenter.needSetDayNightTheme) {
            updateMenuItem();
            checkCurrentDayNight();
        } else if (id == NotificationCenter.emojiPreviewThemesChanged) {
            if (this.themeListRow2 >= 0) {
                this.listAdapter.notifyItemChanged(this.themeListRow2);
            }
        } else if ((id == NotificationCenter.contentSettingsLoaded || id == NotificationCenter.appConfigUpdated) && this.sensitiveContentRow >= 0) {
            this.listAdapter.notifyItemChanged(this.sensitiveContentRow);
        }
    }

    /* JADX INFO: renamed from: lambda$didReceivedNotification$1$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12308lambda$didReceivedNotification$1$orgtelegramuiThemeActivity(DialogInterface dialog) {
        this.sharingProgressDialog = null;
        this.sharingTheme = null;
        this.sharingAccent = null;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(final Context context) {
        this.lastIsDarkTheme = !Theme.isCurrentThemeDay();
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(false);
        if (this.currentType == 3) {
            this.actionBar.setTitle(LocaleController.getString("BrowseThemes", R.string.BrowseThemes));
            ActionBarMenu menu = this.actionBar.createMenu();
            this.sunDrawable = new RLottieDrawable(R.raw.sun, AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), true, null);
            if (this.lastIsDarkTheme) {
                this.sunDrawable.setCurrentFrame(this.sunDrawable.getFramesCount() - 1);
            } else {
                this.sunDrawable.setCurrentFrame(0);
            }
            this.sunDrawable.setPlayInDirectionOfCustomEndFrame(true);
            this.menuItem = menu.addItem(5, this.sunDrawable);
        } else if (this.currentType == 0) {
            this.actionBar.setTitle(LocaleController.getString("ChatSettings", R.string.ChatSettings));
            ActionBarMenu menu2 = this.actionBar.createMenu();
            this.menuItem = menu2.addItem(0, R.drawable.ic_ab_other);
            this.menuItem.setContentDescription(LocaleController.getString("AccDescrMoreOptions", R.string.AccDescrMoreOptions));
            this.menuItem.addSubItem(2, R.drawable.msg_share, LocaleController.getString("ShareTheme", R.string.ShareTheme));
            this.menuItem.addSubItem(3, R.drawable.msg_edit, LocaleController.getString("EditThemeColors", R.string.EditThemeColors));
            this.menuItem.addSubItem(1, R.drawable.msg_palette, LocaleController.getString("CreateNewThemeMenu", R.string.CreateNewThemeMenu));
            this.menuItem.addSubItem(4, R.drawable.msg_reset, LocaleController.getString("ThemeResetToDefaults", R.string.ThemeResetToDefaults));
            if (getMessagesController().getContentSettings() == null) {
                getMessagesController().getContentSettings(new Utilities.Callback() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda8
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        m12300lambda$createView$2$orgtelegramuiThemeActivity((TL_account.contentSettings) obj);
                    }
                });
            }
        } else {
            this.actionBar.setTitle(LocaleController.getString(R.string.AutoNightTheme));
        }
        if (this.parentLayout != null && this.parentLayout.isRightLayout()) {
            this.actionBar.setBackButtonImage(R.drawable.ic_ab_close);
        }
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.fragmentView = frameLayout;
        this.listView = new RecyclerListView(context);
        this.listView.setSections();
        RecyclerListView recyclerListView = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView.setLayoutManager(linearLayoutManager);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setAdapter(this.listAdapter);
        ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(false);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.actionBar.setAdaptiveBackground(this.listView);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda9


            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i, float f, float f2) {
                m12297lambda$createView$13$orgtelegramuiThemeActivity(context, view, i, f, f2);
            }
        });
        if (this.currentType == 0) {
            DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
            itemAnimator.setDurations(350L);
            itemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            itemAnimator.setDelayAnimations(false);
            itemAnimator.setSupportsChangeAnimations(false);
            this.listView.setItemAnimator(itemAnimator);
        }
        if (this.highlightSensitiveRow) {
            updateRows(false);
            this.highlightSensitiveRow = false;
            this.listView.scrollToPosition(this.listAdapter.getItemCount() - 1);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    m12299lambda$createView$15$orgtelegramuiThemeActivity();
                }
            }, 200L);
        }
        return this.fragmentView;
    }

    /* JADX INFO: renamed from: lambda$createView$2$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12300lambda$createView$2$orgtelegramuiThemeActivity(TL_account.contentSettings settings) {
        if (this.listView != null && this.listView.isAttachedToWindow() && this.listAdapter != null) {
            boolean z = false;
            boolean z2 = this.sensitiveContentRow >= 0;
            if (settings != null && settings.sensitive_can_change) {
                z = true;
            }
            if (z2 == z) {
                this.listAdapter.notifyItemChanged(this.sensitiveContentRow);
            } else {
                updateRows(true);
            }
        }
    }

    /* JADX INFO: renamed from: org.telegram.ui.ThemeActivity$1, reason: invalid class name */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int id) {
            Theme.ThemeInfo themeInfo;
            if (id == -1) {
                ThemeActivity.this.finishFragment();
                return;
            }
            if (id == 1) {
                ThemeActivity.this.createNewTheme();
                return;
            }
            if (id == 2) {
                Theme.ThemeInfo currentTheme = Theme.getCurrentTheme();
                Theme.ThemeAccent accent = currentTheme.getAccent(false);
                if (accent.info != null) {
                    String link = "https://" + ThemeActivity.this.getMessagesController().linkPrefix + "/addtheme/" + accent.info.slug;
                    ThemeActivity.this.showDialog(new ShareAlert(ThemeActivity.this.getParentActivity(), null, link, false, link, false));
                    return;
                } else {
                    ThemeActivity.this.getMessagesController().saveThemeToServer(accent.parentTheme, accent);
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needShareTheme, accent.parentTheme, accent);
                    return;
                }
            }
            if (id == 3) {
                ThemeActivity.this.editTheme();
                return;
            }
            if (id == 4) {
                if (ThemeActivity.this.getParentActivity() == null) {
                    return;
                }
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ThemeActivity.this.getParentActivity());
                builder1.setTitle(LocaleController.getString("ThemeResetToDefaultsTitle", R.string.ThemeResetToDefaultsTitle));
                builder1.setMessage(LocaleController.getString("ThemeResetToDefaultsText", R.string.ThemeResetToDefaultsText));
                builder1.setPositiveButton(LocaleController.getString(TimerBuilder.RESET, R.string.Reset), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.ThemeActivity$1$$ExternalSyntheticLambda0
                    @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                    public final void onClick(AlertDialog alertDialog, int i) {
                        m12313lambda$onItemClick$0$orgtelegramuiThemeActivity$1(alertDialog, i);
                    }
                });
                builder1.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                AlertDialog alertDialog = builder1.create();
                ThemeActivity.this.showDialog(alertDialog);
                TextView button = (TextView) alertDialog.getButton(-1);
                if (button != null) {
                    button.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                    return;
                }
                return;
            }
            if (id == 5) {
                SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0);
                String dayThemeName = preferences.getString("lastDayTheme", "Blue");
                if (Theme.getTheme(dayThemeName) == null || Theme.getTheme(dayThemeName).isDark()) {
                    dayThemeName = "Blue";
                }
                String nightThemeName = preferences.getString("lastDarkTheme", "Dark Blue");
                if (Theme.getTheme(nightThemeName) == null || !Theme.getTheme(nightThemeName).isDark()) {
                    nightThemeName = "Dark Blue";
                }
                Theme.ThemeInfo themeInfo2 = Theme.getActiveTheme();
                if (dayThemeName.equals(nightThemeName)) {
                    if (themeInfo2.isDark() || dayThemeName.equals("Dark Blue") || dayThemeName.equals("Night")) {
                        dayThemeName = "Blue";
                    } else {
                        nightThemeName = "Dark Blue";
                    }
                }
                boolean toDark = dayThemeName.equals(themeInfo2.getKey());
                if (toDark) {
                    themeInfo = Theme.getTheme(nightThemeName);
                } else {
                    themeInfo = Theme.getTheme(dayThemeName);
                }
                int[] pos = new int[2];
                ThemeActivity.this.menuItem.getIconView().getLocationInWindow(pos);
                pos[0] = pos[0] + (ThemeActivity.this.menuItem.getIconView().getMeasuredWidth() / 2);
                pos[1] = pos[1] + (ThemeActivity.this.menuItem.getIconView().getMeasuredHeight() / 2);
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, themeInfo, false, pos, -1, Boolean.valueOf(toDark), ThemeActivity.this.menuItem.getIconView());
                ThemeActivity.this.updateRows(true);
                Theme.turnOffAutoNight(ThemeActivity.this);
            }
        }

        /* JADX INFO: renamed from: lambda$onItemClick$0$org-telegram-ui-ThemeActivity$1, reason: not valid java name */
        /* synthetic */ void m12313lambda$onItemClick$0$orgtelegramuiThemeActivity$1(AlertDialog dialogInterface, int i) {
            boolean changed = false;
            if (ThemeActivity.this.setFontSize(AndroidUtilities.isTablet() ? 18 : 16)) {
                changed = true;
            }
            if (ThemeActivity.this.setBubbleRadius(17, true)) {
                changed = true;
            }
            if (changed) {
                ThemeActivity.this.listAdapter.notifyItemChanged(ThemeActivity.this.textSizeRow, new Object());
                ThemeActivity.this.listAdapter.notifyItemChanged(ThemeActivity.this.bubbleRadiusRow, new Object());
            }
            if (ThemeActivity.this.themesHorizontalListCell != null) {
                Theme.ThemeInfo themeInfo = Theme.getTheme("Blue");
                Theme.ThemeInfo currentTheme = Theme.getCurrentTheme();
                Theme.ThemeAccent accent = themeInfo.themeAccentsMap.get(Theme.DEFALT_THEME_ACCENT_ID);
                if (accent != null) {
                    Theme.OverrideWallpaperInfo info = new Theme.OverrideWallpaperInfo();
                    info.slug = "d";
                    info.fileName = "Blue_99_wp.jpg";
                    info.originalFileName = "Blue_99_wp.jpg";
                    accent.overrideWallpaper = info;
                    themeInfo.setOverrideWallpaper(info);
                }
                if (themeInfo != currentTheme) {
                    themeInfo.setCurrentAccentId(Theme.DEFALT_THEME_ACCENT_ID);
                    Theme.saveThemeAccents(themeInfo, true, false, true, false);
                    ThemeActivity.this.themesHorizontalListCell.selectTheme(themeInfo);
                    ThemeActivity.this.themesHorizontalListCell.smoothScrollToPosition(0);
                    return;
                }
                if (themeInfo.currentAccentId != Theme.DEFALT_THEME_ACCENT_ID) {
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, currentTheme, Boolean.valueOf(ThemeActivity.this.currentType == 1), null, Integer.valueOf(Theme.DEFALT_THEME_ACCENT_ID));
                    ThemeActivity.this.listAdapter.notifyItemChanged(ThemeActivity.this.themeAccentListRow);
                } else {
                    Theme.reloadWallpaper(true);
                }
            }
        }
    }

    /* JADX INFO: renamed from: lambda$createView$13$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12297lambda$createView$13$orgtelegramuiThemeActivity(Context context, final View view, final int position, float x, float y) {
        int currentHour;
        int currentMinute;
        boolean z;
        String value;
        String type;
        if (position != this.enableAnimationsRow) {
            if (position == this.backgroundRow) {
                presentFragment(new WallpapersListActivity(0));
                return;
            }
            if (position == this.changeUserColor) {
                presentFragment(new PeerColorActivity(0L).setOnApplied(this));
                return;
            }
            if (position != this.sendByEnterRow) {
                if (position == this.raiseToSpeakRow) {
                    SharedConfig.toggleRaiseToSpeak();
                    if (view instanceof TextCheckCell) {
                        ((TextCheckCell) view).setChecked(SharedConfig.raiseToSpeak);
                        return;
                    }
                    return;
                }
                if (position == this.nextMediaTapRow) {
                    SharedConfig.toggleNextMediaTap();
                    if (view instanceof TextCheckCell) {
                        ((TextCheckCell) view).setChecked(SharedConfig.nextMediaTap);
                        return;
                    }
                    return;
                }
                if (position == this.raiseToListenRow) {
                    SharedConfig.toggleRaiseToListen();
                    if (view instanceof TextCheckCell) {
                        ((TextCheckCell) view).setChecked(SharedConfig.raiseToListen);
                    }
                    if (!SharedConfig.raiseToListen && this.raiseToSpeakRow != -1) {
                        for (int i = 0; i < this.listView.getChildCount(); i++) {
                            View child = this.listView.getChildAt(i);
                            if ((child instanceof TextCheckCell) && this.listView.getChildAdapterPosition(child) == this.raiseToSpeakRow) {
                                ((TextCheckCell) child).setChecked(false);
                            }
                        }
                    }
                    updateRows(false);
                    return;
                }
                if (position == this.pauseOnRecordRow) {
                    SharedConfig.togglePauseMusicOnRecord();
                    if (view instanceof TextCheckCell) {
                        ((TextCheckCell) view).setChecked(SharedConfig.pauseMusicOnRecord);
                        return;
                    }
                    return;
                }
                if (position == this.pauseOnMediaRow) {
                    SharedConfig.togglePauseMusicOnMedia();
                    if (view instanceof TextCheckCell) {
                        ((TextCheckCell) view).setChecked(SharedConfig.pauseMusicOnMedia);
                        return;
                    }
                    return;
                }
                if (position != this.distanceRow) {
                    if (position != this.searchEngineRow) {
                        if (position != this.bluetoothScoRow) {
                            if (position == this.directShareRow) {
                                SharedConfig.toggleDirectShare();
                                if (view instanceof TextCheckCell) {
                                    ((TextCheckCell) view).setChecked(SharedConfig.directShare);
                                    return;
                                }
                                return;
                            }
                            if (position == this.sensitiveContentRow) {
                                if (!getMessagesController().showSensitiveContent()) {
                                    final Runnable set = new Runnable() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda24
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            m12306lambda$createView$8$orgtelegramuiThemeActivity(view);
                                        }
                                    };
                                    showDialog(new AlertDialog.Builder(context, this.resourceProvider).setTitle(LocaleController.getString(R.string.ConfirmSensitiveContentTitle)).setMessage(LocaleController.getString(R.string.ConfirmSensitiveContentText)).setPositiveButton(LocaleController.getString(R.string.Confirm), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda1
                                        @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                                        public final void onClick(AlertDialog alertDialog, int i2) {
                                            m12294lambda$createView$10$orgtelegramuiThemeActivity(set, alertDialog, i2);
                                        }
                                    }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).create());
                                    return;
                                } else {
                                    getMessagesController().setContentSettings(false);
                                    if (view instanceof TextCheckCell) {
                                        ((TextCheckCell) view).setChecked(getMessagesController().showSensitiveContent());
                                        return;
                                    }
                                    return;
                                }
                            }
                            if (position != this.contactsReimportRow) {
                                if (position != this.contactsSortRow) {
                                    if (position == this.chatBlurRow) {
                                        SharedConfig.toggleChatBlur();
                                        if (view instanceof TextCheckCell) {
                                            ((TextCheckCell) view).setChecked(SharedConfig.chatBlurEnabled());
                                            return;
                                        }
                                        return;
                                    }
                                    if (position == this.nightThemeRow) {
                                        if ((!LocaleController.isRTL || x > AndroidUtilities.dp(76.0f)) && (LocaleController.isRTL || x < view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) {
                                            presentFragment(new ThemeActivity(1));
                                            return;
                                        }
                                        NotificationsCheckCell checkCell = (NotificationsCheckCell) view;
                                        if (Theme.selectedAutoNightType == 0) {
                                            Theme.selectedAutoNightType = 2;
                                            z = true;
                                            checkCell.setChecked(true);
                                        } else {
                                            z = true;
                                            Theme.selectedAutoNightType = 0;
                                            checkCell.setChecked(false);
                                        }
                                        Theme.saveAutoNightThemeConfig();
                                        Theme.checkAutoNightThemeConditions(z);
                                        boolean enabled = Theme.selectedAutoNightType != 0;
                                        String value2 = enabled ? Theme.getCurrentNightThemeName() : LocaleController.getString("AutoNightThemeOff", R.string.AutoNightThemeOff);
                                        if (!enabled) {
                                            value = value2;
                                        } else {
                                            if (Theme.selectedAutoNightType == 1) {
                                                type = LocaleController.getString("AutoNightScheduled", R.string.AutoNightScheduled);
                                            } else if (Theme.selectedAutoNightType == 3) {
                                                type = LocaleController.getString("AutoNightSystemDefault", R.string.AutoNightSystemDefault);
                                            } else {
                                                type = LocaleController.getString("AutoNightAdaptive", R.string.AutoNightAdaptive);
                                            }
                                            value = type + " " + value2;
                                        }
                                        checkCell.setTextAndValueAndIconAndCheck(LocaleController.getString("AutoNightTheme", R.string.AutoNightTheme), value, R.drawable.menu_night_mode_24, enabled, 0, false, true);
                                        return;
                                    }
                                    if (position == this.browserRow) {
                                        if ((!LocaleController.isRTL || x > AndroidUtilities.dp(76.0f)) && (LocaleController.isRTL || x < view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) {
                                            presentFragment(new WebBrowserSettings(null));
                                            return;
                                        }
                                        NotificationsCheckCell checkCell2 = (NotificationsCheckCell) view;
                                        getMessagesController().toggleWebBrowserInAppEnabled();
                                        boolean inAppBrowserEnabled = getMessagesController().isWebBrowserInAppEnabled();
                                        checkCell2.setChecked(inAppBrowserEnabled);
                                        return;
                                    }
                                    if (position != this.nightDisabledRow) {
                                        if (position == this.nightScheduledRow) {
                                            if (Theme.selectedAutoNightType == 1) {
                                                return;
                                            }
                                            Theme.selectedAutoNightType = 1;
                                            if (Theme.autoNightScheduleByLocation) {
                                                updateSunTime(null, true);
                                            }
                                            updateRows(true);
                                            Theme.checkAutoNightThemeConditions();
                                            return;
                                        }
                                        if (position == this.nightAutomaticRow) {
                                            if (Theme.selectedAutoNightType == 2) {
                                                return;
                                            }
                                            Theme.selectedAutoNightType = 2;
                                            updateRows(true);
                                            Theme.checkAutoNightThemeConditions();
                                            return;
                                        }
                                        if (position == this.nightSystemDefaultRow) {
                                            if (Theme.selectedAutoNightType == 3) {
                                                return;
                                            }
                                            Theme.selectedAutoNightType = 3;
                                            updateRows(true);
                                            Theme.checkAutoNightThemeConditions();
                                            return;
                                        }
                                        if (position != this.scheduleLocationRow) {
                                            if (position == this.scheduleFromRow || position == this.scheduleToRow) {
                                                if (getParentActivity() == null) {
                                                    return;
                                                }
                                                if (position == this.scheduleFromRow) {
                                                    int currentHour2 = Theme.autoNightDayStartTime / 60;
                                                    currentHour = currentHour2;
                                                    currentMinute = Theme.autoNightDayStartTime - (currentHour2 * 60);
                                                } else {
                                                    int currentHour3 = Theme.autoNightDayEndTime / 60;
                                                    currentHour = currentHour3;
                                                    currentMinute = Theme.autoNightDayEndTime - (currentHour3 * 60);
                                                }
                                                final TextSettingsCell cell = (TextSettingsCell) view;
                                                showDialog(new TimePickerDialog(getParentActivity(), new TimePickerDialog.OnTimeSetListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda3
                                                    @Override // android.app.TimePickerDialog.OnTimeSetListener
                                                    public final void onTimeSet(TimePicker timePicker, int i2, int i3) {
                                                        m12296lambda$createView$12$orgtelegramuiThemeActivity(position, cell, timePicker, i2, i3);
                                                    }
                                                }, currentHour, currentMinute, true));
                                                return;
                                            }
                                            if (position == this.scheduleUpdateLocationRow) {
                                                updateSunTime(null, true);
                                                return;
                                            }
                                            if (position == this.createNewThemeRow) {
                                                createNewTheme();
                                                return;
                                            }
                                            if (position == this.editThemeRow) {
                                                editTheme();
                                                return;
                                            } else if (position == this.stickersRow) {
                                                presentFragment(new StickersActivity(0, null));
                                                return;
                                            } else {
                                                if (position == this.liteModeRow) {
                                                    presentFragment(new LiteModeSettingsActivity());
                                                    return;
                                                }
                                                return;
                                            }
                                        }
                                        Theme.autoNightScheduleByLocation = !Theme.autoNightScheduleByLocation;
                                        TextCheckCell checkCell3 = (TextCheckCell) view;
                                        checkCell3.setChecked(Theme.autoNightScheduleByLocation);
                                        updateRows(true);
                                        if (Theme.autoNightScheduleByLocation) {
                                            updateSunTime(null, true);
                                        }
                                        Theme.checkAutoNightThemeConditions();
                                        return;
                                    }
                                    if (Theme.selectedAutoNightType == 0) {
                                        return;
                                    }
                                    Theme.selectedAutoNightType = 0;
                                    updateRows(true);
                                    Theme.checkAutoNightThemeConditions();
                                    return;
                                }
                                if (getParentActivity() == null) {
                                    return;
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                                builder.setTitle(LocaleController.getString("SortBy", R.string.SortBy));
                                builder.setItems(new CharSequence[]{LocaleController.getString(Profile.DEFAULT_PROFILE_NAME, R.string.Default), LocaleController.getString("SortFirstName", R.string.SortFirstName), LocaleController.getString("SortLastName", R.string.SortLastName)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda2
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public final void onClick(DialogInterface dialogInterface, int i2) {
                                        m12295lambda$createView$11$orgtelegramuiThemeActivity(position, dialogInterface, i2);
                                    }
                                });
                                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                                showDialog(builder.create());
                                return;
                            }
                            return;
                        }
                        if (getParentActivity() == null) {
                            return;
                        }
                        final AtomicReference<Dialog> dialogRef = new AtomicReference<>();
                        LinearLayout linearLayout = new LinearLayout(context);
                        linearLayout.setOrientation(1);
                        RadioColorCell cell2 = new RadioColorCell(getParentActivity());
                        cell2.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                        cell2.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
                        cell2.setTextAndValue(LocaleController.getString(R.string.MicrophoneForVoiceMessagesBuiltIn), !SharedConfig.recordViaSco);
                        cell2.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 2));
                        linearLayout.addView(cell2);
                        cell2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda22
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                m12303lambda$createView$5$orgtelegramuiThemeActivity(dialogRef, view2);
                            }
                        });
                        RadioColorCell cell3 = new RadioColorCell(getParentActivity());
                        cell3.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                        cell3.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
                        cell3.setTextAndText2AndValue(LocaleController.getString(R.string.MicrophoneForVoiceMessagesScoIfConnected), LocaleController.getString(R.string.MicrophoneForVoiceMessagesScoHint), SharedConfig.recordViaSco);
                        cell3.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 2));
                        linearLayout.addView(cell3);
                        cell3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda23
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                m12305lambda$createView$7$orgtelegramuiThemeActivity(dialogRef, view2);
                            }
                        });
                        Dialog dialog = new AlertDialog.Builder(getParentActivity()).setTitle(LocaleController.getString(R.string.MicrophoneForVoiceMessages)).setView(linearLayout).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null).create();
                        dialogRef.set(dialog);
                        showDialog(dialog);
                        return;
                    }
                    if (getParentActivity() == null) {
                        return;
                    }
                    final AtomicReference<Dialog> dialogRef2 = new AtomicReference<>();
                    LinearLayout linearLayout2 = new LinearLayout(context);
                    linearLayout2.setOrientation(1);
                    ArrayList<SearchEngine> searchEngines = SearchEngine.getSearchEngines();
                    CharSequence[] items = new CharSequence[searchEngines.size()];
                    for (int i2 = 0; i2 < items.length; i2++) {
                        SearchEngine engine = searchEngines.get(i2);
                        items[i2] = engine.name;
                        final int index = i2;
                        RadioColorCell cell4 = new RadioColorCell(getParentActivity());
                        cell4.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                        cell4.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
                        cell4.setTextAndValue(items[index], index == SharedConfig.searchEngineType);
                        cell4.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 2));
                        linearLayout2.addView(cell4);
                        cell4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda21
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                m12302lambda$createView$4$orgtelegramuiThemeActivity(index, dialogRef2, view2);
                            }
                        });
                    }
                    Dialog dialog2 = new AlertDialog.Builder(getParentActivity()).setTitle(LocaleController.getString(R.string.SearchEngine)).setView(linearLayout2).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null).create();
                    dialogRef2.set(dialog2);
                    showDialog(dialog2);
                    return;
                }
                if (getParentActivity() == null) {
                    return;
                }
                final AtomicReference<Dialog> dialogRef3 = new AtomicReference<>();
                LinearLayout linearLayout3 = new LinearLayout(context);
                linearLayout3.setOrientation(1);
                CharSequence[] items2 = {LocaleController.getString("DistanceUnitsAutomatic", R.string.DistanceUnitsAutomatic), LocaleController.getString("DistanceUnitsKilometers", R.string.DistanceUnitsKilometers), LocaleController.getString("DistanceUnitsMiles", R.string.DistanceUnitsMiles)};
                for (int i3 = 0; i3 < items2.length; i3++) {
                    final int index2 = i3;
                    RadioColorCell cell5 = new RadioColorCell(getParentActivity());
                    cell5.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                    cell5.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
                    cell5.setTextAndValue(items2[index2], index2 == SharedConfig.distanceSystemType);
                    cell5.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 2));
                    linearLayout3.addView(cell5);
                    cell5.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda20
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            m12301lambda$createView$3$orgtelegramuiThemeActivity(index2, dialogRef3, view2);
                        }
                    });
                }
                Dialog dialog3 = new AlertDialog.Builder(getParentActivity()).setTitle(LocaleController.getString("DistanceUnitsTitle", R.string.DistanceUnitsTitle)).setView(linearLayout3).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null).create();
                dialogRef3.set(dialog3);
                showDialog(dialog3);
                return;
            }
            SharedPreferences preferences = MessagesController.getGlobalMainSettings();
            boolean send = preferences.getBoolean("send_by_enter", false);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("send_by_enter", !send);
            editor.commit();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(!send);
                return;
            }
            return;
        }
        SharedPreferences preferences2 = MessagesController.getGlobalMainSettings();
        boolean animations = preferences2.getBoolean("view_animations", true);
        SharedPreferences.Editor editor2 = preferences2.edit();
        editor2.putBoolean("view_animations", !animations);
        SharedConfig.setAnimationsEnabled(!animations);
        editor2.commit();
        if (view instanceof TextCheckCell) {
            ((TextCheckCell) view).setChecked(!animations);
        }
    }

    /* JADX INFO: renamed from: lambda$createView$3$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12301lambda$createView$3$orgtelegramuiThemeActivity(int index, AtomicReference dialogRef, View v) {
        SharedConfig.setDistanceSystemType(index);
        this.updateDistance = true;
        RecyclerView.ViewHolder holder = this.listView.findViewHolderForAdapterPosition(this.distanceRow);
        if (holder != null) {
            this.listAdapter.onBindViewHolder(holder, this.distanceRow);
        }
        ((Dialog) dialogRef.get()).dismiss();
    }

    /* JADX INFO: renamed from: lambda$createView$4$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12302lambda$createView$4$orgtelegramuiThemeActivity(int index, AtomicReference dialogRef, View v) {
        SharedConfig.setSearchEngineType(index);
        this.updateSearchEngine = true;
        RecyclerView.ViewHolder holder = this.listView.findViewHolderForAdapterPosition(this.searchEngineRow);
        if (holder != null) {
            this.listAdapter.onBindViewHolder(holder, this.searchEngineRow);
        }
        ((Dialog) dialogRef.get()).dismiss();
    }

    /* JADX INFO: renamed from: lambda$createView$5$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12303lambda$createView$5$orgtelegramuiThemeActivity(AtomicReference dialogRef, View v) {
        SharedConfig.recordViaSco = false;
        SharedConfig.saveConfig();
        this.updateRecordViaSco = true;
        ((Dialog) dialogRef.get()).dismiss();
        RecyclerView.ViewHolder holder = this.listView.findViewHolderForAdapterPosition(this.bluetoothScoRow);
        if (holder != null) {
            this.listAdapter.onBindViewHolder(holder, this.bluetoothScoRow);
        }
    }

    /* JADX INFO: renamed from: lambda$createView$7$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12305lambda$createView$7$orgtelegramuiThemeActivity(final AtomicReference dialogRef, View v) {
        PermissionRequest.ensurePermission(R.raw.permission_request_microphone, R.string.PermissionNoBluetoothWithHint, "android.permission.BLUETOOTH_CONNECT", new Utilities.Callback() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda11
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                m12304lambda$createView$6$orgtelegramuiThemeActivity(dialogRef, (Boolean) obj);
            }
        });
        SharedConfig.recordViaSco = true;
        SharedConfig.saveConfig();
        this.updateRecordViaSco = true;
        ((Dialog) dialogRef.get()).dismiss();
        RecyclerView.ViewHolder holder = this.listView.findViewHolderForAdapterPosition(this.bluetoothScoRow);
        if (holder != null) {
            this.listAdapter.onBindViewHolder(holder, this.bluetoothScoRow);
        }
    }

    /* JADX INFO: renamed from: lambda$createView$6$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12304lambda$createView$6$orgtelegramuiThemeActivity(AtomicReference dialogRef, Boolean granted) {
        RecyclerView.ViewHolder holder;
        if (!granted.booleanValue()) {
            SharedConfig.recordViaSco = false;
            SharedConfig.saveConfig();
            this.updateRecordViaSco = true;
            ((Dialog) dialogRef.get()).dismiss();
            if (this.listView != null && this.listView.isAttachedToWindow() && (holder = this.listView.findViewHolderForAdapterPosition(this.bluetoothScoRow)) != null) {
                this.listAdapter.onBindViewHolder(holder, this.bluetoothScoRow);
            }
        }
    }

    /* JADX INFO: renamed from: lambda$createView$8$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12306lambda$createView$8$orgtelegramuiThemeActivity(View view) {
        getMessagesController().setContentSettings(true);
        if (view instanceof TextCheckCell) {
            ((TextCheckCell) view).setChecked(getMessagesController().showSensitiveContent());
        }
    }

    /* JADX INFO: renamed from: lambda$createView$10$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12294lambda$createView$10$orgtelegramuiThemeActivity(final Runnable set, AlertDialog di, int w) {
        verifyAge(getContext(), this.currentAccount, new Utilities.Callback() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda14
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                m12307lambda$createView$9$orgtelegramuiThemeActivity(set, (Boolean) obj);
            }
        }, getResourceProvider());
    }

    /* JADX INFO: renamed from: lambda$createView$9$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12307lambda$createView$9$orgtelegramuiThemeActivity(Runnable set, Boolean passed) {
        if (passed.booleanValue()) {
            set.run();
        } else {
            BulletinFactory.of(this).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.AgeVerificationFailedTitle), LocaleController.getString(R.string.AgeVerificationFailedText)).show();
        }
    }

    /* JADX INFO: renamed from: lambda$createView$11$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12295lambda$createView$11$orgtelegramuiThemeActivity(int position, DialogInterface dialog, int which) {
        SharedPreferences preferences = MessagesController.getGlobalMainSettings();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("sortContactsBy", which);
        editor.commit();
        if (this.listAdapter != null) {
            this.listAdapter.notifyItemChanged(position);
        }
    }

    /* JADX INFO: renamed from: lambda$createView$12$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12296lambda$createView$12$orgtelegramuiThemeActivity(int position, TextSettingsCell cell, TimePicker view1, int hourOfDay, int minute) {
        int time = (hourOfDay * 60) + minute;
        if (position == this.scheduleFromRow) {
            Theme.autoNightDayStartTime = time;
            cell.setTextAndValue(LocaleController.getString("AutoNightFrom", R.string.AutoNightFrom), String.format("%02d:%02d", Integer.valueOf(hourOfDay), Integer.valueOf(minute)), true);
        } else {
            Theme.autoNightDayEndTime = time;
            cell.setTextAndValue(LocaleController.getString("AutoNightTo", R.string.AutoNightTo), String.format("%02d:%02d", Integer.valueOf(hourOfDay), Integer.valueOf(minute)), true);
        }
    }

    /* JADX INFO: renamed from: lambda$createView$14$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ int m12298lambda$createView$14$orgtelegramuiThemeActivity() {
        return this.sensitiveContentRow;
    }

    /* JADX INFO: renamed from: lambda$createView$15$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12299lambda$createView$15$orgtelegramuiThemeActivity() {
        this.listView.highlightRow(new RecyclerListView.IntReturnCallback() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda7
            @Override // org.telegram.ui.Components.RecyclerListView.IntReturnCallback
            public final int run() {
                return m12298lambda$createView$14$orgtelegramuiThemeActivity();
            }
        });
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSupportEdgeToEdge() {
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onInsets(int left, int top, int right, int bottom) {
        this.listView.setPadding(0, 0, 0, bottom);
        this.listView.setClipToPadding(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void editTheme() {
        Theme.ThemeInfo currentTheme = Theme.getCurrentTheme();
        Theme.ThemeAccent accent = currentTheme.getAccent(false);
        presentFragment(new ThemePreviewActivity(currentTheme, false, 1, accent.id >= 100, this.currentType == 1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createNewTheme() {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("NewTheme", R.string.NewTheme));
        builder.setMessage(LocaleController.getString("CreateNewThemeAlert", R.string.CreateNewThemeAlert));
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        builder.setPositiveButton(LocaleController.getString("CreateTheme", R.string.CreateTheme), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda19
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                m12293lambda$createNewTheme$16$orgtelegramuiThemeActivity(alertDialog, i);
            }
        });
        showDialog(builder.create());
    }

    /* JADX INFO: renamed from: lambda$createNewTheme$16$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12293lambda$createNewTheme$16$orgtelegramuiThemeActivity(AlertDialog dialog, int which) {
        AlertsCreator.createThemeCreateDialog(this, 0, null, null);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        if (this.listAdapter != null) {
            updateRows(true);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean isOpen, boolean backward) {
        if (isOpen) {
            AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
            AndroidUtilities.setAdjustResizeToNothing(getParentActivity(), this.classGuid);
        }
    }

    public static void verifyAge(final Context context, final int currentAccount, final Utilities.Callback<Boolean> done, final Theme.ResourcesProvider resourcesProvider) {
        final MessagesController messagesController = MessagesController.getInstance(currentAccount);
        final String username = messagesController.verifyAgeBotUsername;
        String country = messagesController.verifyAgeCountry;
        final int minAge = messagesController.verifyAgeMin;
        if (!TextUtils.isEmpty(username) && messagesController.config.needAgeVideoVerification.get()) {
            BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(1);
            linearLayout.setPadding(AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f), 0);
            linearLayout.setClipChildren(false);
            linearLayout.setClipToPadding(false);
            builder.setCustomView(linearLayout);
            FrameLayout topView = new FrameLayout(context);
            topView.setBackground(Theme.createCircleDrawable(AndroidUtilities.dp(80.0f), Theme.getColor(Theme.key_featuredStickers_addButton, resourcesProvider)));
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setImageResource(R.drawable.filled_verify_age);
            topView.addView(imageView, LayoutHelper.createFrame(50, 50, 17));
            linearLayout.addView(topView, LayoutHelper.createLinear(80, 80, 1, 0, 20, 0, 8));
            TextView textView = TextHelper.makeTextView(context, 20.0f, Theme.key_dialogTextBlack, true, resourcesProvider);
            textView.setText(LocaleController.getString(R.string.AgeVerificationTitle));
            textView.setGravity(17);
            linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 7, 24, 8, 24, 8));
            TextView textView2 = TextHelper.makeTextView(context, 14.0f, Theme.key_dialogTextBlack, false, resourcesProvider);
            textView2.setText(AndroidUtilities.replaceTags(LocaleController.getString("AgeVerificationText" + country)));
            textView2.setGravity(17);
            linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2, 7, 24, 0, 24, 0));
            final BottomSheet[] sheet = new BottomSheet[1];
            final ButtonWithCounterView button = new ButtonWithCounterView(context, resourcesProvider);
            button.setText(LocaleController.getString(R.string.AgeVerificationButton), false);
            button.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda17
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ThemeActivity.lambda$verifyAge$20(button, messagesController, username, currentAccount, context, resourcesProvider, minAge, done, sheet, view);
                }
            });
            linearLayout.addView(button, LayoutHelper.createLinear(-1, 48, 7, 2, 29, 2, 14));
            sheet[0] = builder.show();
            sheet[0].fixNavigationBar();
            return;
        }
        done.run(true);
    }

    static /* synthetic */ void lambda$verifyAge$20(final ButtonWithCounterView button, final MessagesController messagesController, final String username, final int currentAccount, final Context context, final Theme.ResourcesProvider resourcesProvider, final int minAge, final Utilities.Callback done, final BottomSheet[] sheet, View v) {
        if (button.isLoading()) {
            return;
        }
        button.setLoading(true);
        PermissionRequest.ensurePermission(R.raw.permission_request_camera, R.string.AgeVerificationNeedCameraPermission, "android.permission.CAMERA", new Utilities.Callback() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda4
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                ThemeActivity.lambda$verifyAge$19(button, messagesController, username, currentAccount, context, resourcesProvider, minAge, done, sheet, (Boolean) obj);
            }
        });
    }

    static /* synthetic */ void lambda$verifyAge$19(final ButtonWithCounterView button, final MessagesController messagesController, String username, final int currentAccount, final Context context, final Theme.ResourcesProvider resourcesProvider, final int minAge, final Utilities.Callback done, final BottomSheet[] sheet, Boolean granted) {
        if (!granted.booleanValue()) {
            button.setLoading(false);
        } else {
            messagesController.getUserNameResolver().resolve(username, new Utilities.Callback() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda5
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    ThemeActivity.lambda$verifyAge$18(button, messagesController, currentAccount, context, resourcesProvider, minAge, done, sheet, (Long) obj);
                }
            });
        }
    }

    static /* synthetic */ void lambda$verifyAge$18(ButtonWithCounterView button, MessagesController messagesController, int currentAccount, Context context, Theme.ResourcesProvider resourcesProvider, final int minAge, final Utilities.Callback done, BottomSheet[] sheet, Long userId) {
        if (userId == null) {
            button.setLoading(false);
            return;
        }
        TLRPC.User bot = messagesController.getUser(userId);
        if (bot == null) {
            button.setLoading(false);
            return;
        }
        BaseFragment lastFragment = LaunchActivity.getSafeLastFragment();
        if (lastFragment == null) {
            button.setLoading(false);
            return;
        }
        WebViewRequestProps props = WebViewRequestProps.of(currentAccount, userId.longValue(), userId.longValue(), null, null, 4, 0, 0L, false, null, false, null, bot, 0, false, false);
        final BotWebViewSheet webViewSheet = new BotWebViewSheet(context, resourcesProvider);
        webViewSheet.setOnVerifiedAge(new Utilities.Callback4() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda0
            @Override // org.telegram.messenger.Utilities.Callback4
            public final void run(Object obj, Object obj2, Object obj3, Object obj4) {
                ThemeActivity.lambda$verifyAge$17(minAge, webViewSheet, done, (Boolean) obj, (Double) obj2, (String) obj3, (Double) obj4);
            }
        });
        webViewSheet.setDefaultFullsize(true);
        webViewSheet.setNeedsContext(false);
        webViewSheet.setParentActivity(lastFragment.getParentActivity());
        webViewSheet.requestWebView(lastFragment, props);
        webViewSheet.show();
        button.setLoading(false);
        sheet[0].dismiss();
    }

    static /* synthetic */ void lambda$verifyAge$17(int minAge, BotWebViewSheet webViewSheet, Utilities.Callback done, Boolean localPassed, Double age, String gender, Double genderProbability) {
        boolean passed = age != null ? age.doubleValue() >= ((double) minAge) : localPassed.booleanValue();
        webViewSheet.dismiss();
        done.run(Boolean.valueOf(passed));
        BaseFragment lastFragment2 = LaunchActivity.getSafeLastFragment();
        if (passed && lastFragment2 != null) {
            BulletinFactory.of(lastFragment2).createSimpleBulletin(R.raw.contact_check, LocaleController.getString(R.string.AgeVerificationPassedTitle)).show();
        }
    }

    private void updateMenuItem() {
        if (this.menuItem == null) {
            return;
        }
        Theme.ThemeInfo themeInfo = Theme.getCurrentTheme();
        Theme.ThemeAccent accent = themeInfo.getAccent(false);
        if (themeInfo.themeAccents != null && !themeInfo.themeAccents.isEmpty() && accent != null && accent.id >= 100) {
            this.menuItem.showSubItem(2);
            this.menuItem.showSubItem(3);
        } else {
            this.menuItem.hideSubItem(2);
            this.menuItem.hideSubItem(3);
        }
        int fontSize = AndroidUtilities.isTablet() ? 18 : 16;
        Theme.ThemeInfo currentTheme = Theme.getCurrentTheme();
        if (SharedConfig.fontSize != fontSize || SharedConfig.bubbleRadius != 17 || !currentTheme.firstAccentIsDefault || currentTheme.currentAccentId != Theme.DEFALT_THEME_ACCENT_ID || (accent != null && accent.overrideWallpaper != null && !"d".equals(accent.overrideWallpaper.slug))) {
            this.menuItem.showSubItem(4);
        } else {
            this.menuItem.hideSubItem(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSunTime(Location lastKnownLocation, boolean forceUpdate) {
        Activity activity;
        LocationManager locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
        if (Build.VERSION.SDK_INT >= 23 && (activity = getParentActivity()) != null && activity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
            activity.requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
            return;
        }
        if (getParentActivity() != null) {
            if (!getParentActivity().getPackageManager().hasSystemFeature("android.hardware.location.gps")) {
                return;
            }
            try {
                LocationManager lm = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
                if (!lm.isProviderEnabled("gps")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                    builder.setTopAnimation(R.raw.permission_request_location, 72, false, Theme.getColor(Theme.key_dialogTopBackground));
                    builder.setMessage(LocaleController.getString("GpsDisabledAlertText", R.string.GpsDisabledAlertText));
                    builder.setPositiveButton(LocaleController.getString("ConnectingToProxyEnable", R.string.ConnectingToProxyEnable), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda15
                        @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                        public final void onClick(AlertDialog alertDialog, int i) {
                            m12310lambda$updateSunTime$21$orgtelegramuiThemeActivity(alertDialog, i);
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    showDialog(builder.create());
                    return;
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        try {
            lastKnownLocation = locationManager.getLastKnownLocation("gps");
            if (lastKnownLocation == null) {
                lastKnownLocation = locationManager.getLastKnownLocation("network");
            }
            if (lastKnownLocation == null) {
                lastKnownLocation = locationManager.getLastKnownLocation("passive");
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        if (lastKnownLocation == null || forceUpdate) {
            startLocationUpdate();
            if (lastKnownLocation == null) {
                return;
            }
        }
        Theme.autoNightLocationLatitude = lastKnownLocation.getLatitude();
        Theme.autoNightLocationLongitude = lastKnownLocation.getLongitude();
        int[] time = SunDate.calculateSunriseSunset(Theme.autoNightLocationLatitude, Theme.autoNightLocationLongitude);
        Theme.autoNightSunriseTime = time[0];
        Theme.autoNightSunsetTime = time[1];
        Theme.autoNightCityName = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Theme.autoNightLastSunCheckDay = calendar.get(5);
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                m12312lambda$updateSunTime$23$orgtelegramuiThemeActivity();
            }
        });
        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findViewHolderForAdapterPosition(this.scheduleLocationInfoRow);
        if (holder != null && (holder.itemView instanceof TextInfoPrivacyCell)) {
            ((TextInfoPrivacyCell) holder.itemView).setText(getLocationSunString());
        }
        if (Theme.autoNightScheduleByLocation && Theme.selectedAutoNightType == 1) {
            Theme.checkAutoNightThemeConditions();
        }
    }

    /* JADX INFO: renamed from: lambda$updateSunTime$21$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12310lambda$updateSunTime$21$orgtelegramuiThemeActivity(AlertDialog dialog, int id) {
        if (getParentActivity() == null) {
            return;
        }
        try {
            getParentActivity().startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
        } catch (Exception e) {
        }
    }

    /* JADX INFO: renamed from: lambda$updateSunTime$23$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12312lambda$updateSunTime$23$orgtelegramuiThemeActivity() {
        String name;
        try {
            Geocoder gcd = new Geocoder(ApplicationLoader.applicationContext, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(Theme.autoNightLocationLatitude, Theme.autoNightLocationLongitude, 1);
            if (addresses.size() > 0) {
                name = addresses.get(0).getLocality();
            } else {
                name = null;
            }
        } catch (Exception e) {
            name = null;
        }
        final String nameFinal = name;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                m12311lambda$updateSunTime$22$orgtelegramuiThemeActivity(nameFinal);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$updateSunTime$22$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12311lambda$updateSunTime$22$orgtelegramuiThemeActivity(String nameFinal) {
        RecyclerListView.Holder holder;
        Theme.autoNightCityName = nameFinal;
        if (Theme.autoNightCityName == null) {
            Theme.autoNightCityName = String.format("(%.06f, %.06f)", Double.valueOf(Theme.autoNightLocationLatitude), Double.valueOf(Theme.autoNightLocationLongitude));
        }
        Theme.saveAutoNightThemeConfig();
        if (this.listView != null && (holder = (RecyclerListView.Holder) this.listView.findViewHolderForAdapterPosition(this.scheduleUpdateLocationRow)) != null && (holder.itemView instanceof TextSettingsCell)) {
            ((TextSettingsCell) holder.itemView).setTextAndValue(LocaleController.getString("AutoNightUpdateLocation", R.string.AutoNightUpdateLocation), Theme.autoNightCityName, false);
        }
    }

    private void startLocationUpdate() {
        if (this.updatingLocation) {
            return;
        }
        this.updatingLocation = true;
        LocationManager locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
        try {
            locationManager.requestLocationUpdates("gps", 1L, 0.0f, this.gpsLocationListener);
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            locationManager.requestLocationUpdates("network", 1L, 0.0f, this.networkLocationListener);
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopLocationUpdate() {
        this.updatingLocation = false;
        LocationManager locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
        locationManager.removeUpdates(this.gpsLocationListener);
        locationManager.removeUpdates(this.networkLocationListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getLocationSunString() {
        int currentHour = Theme.autoNightSunriseTime / 60;
        int currentMinute = Theme.autoNightSunriseTime - (currentHour * 60);
        String sunriseTimeStr = String.format("%02d:%02d", Integer.valueOf(currentHour), Integer.valueOf(currentMinute));
        int currentHour2 = Theme.autoNightSunsetTime / 60;
        int currentMinute2 = Theme.autoNightSunsetTime - (currentHour2 * 60);
        String sunsetTimeStr = String.format("%02d:%02d", Integer.valueOf(currentHour2), Integer.valueOf(currentMinute2));
        return LocaleController.formatString("AutoNightUpdateLocationInfo", R.string.AutoNightUpdateLocationInfo, sunsetTimeStr, sunriseTimeStr);
    }

    private static class InnerAccentView extends View {
        private ObjectAnimator checkAnimator;
        private boolean checked;
        private float checkedState;
        private Theme.ThemeAccent currentAccent;
        private Theme.ThemeInfo currentTheme;
        private final Paint paint;

        InnerAccentView(Context context) {
            super(context);
            this.paint = new Paint(1);
        }

        void setThemeAndColor(Theme.ThemeInfo themeInfo, Theme.ThemeAccent accent) {
            this.currentTheme = themeInfo;
            this.currentAccent = accent;
            updateCheckedState(false);
        }

        void updateCheckedState(boolean animate) {
            this.checked = this.currentTheme.currentAccentId == this.currentAccent.id;
            if (this.checkAnimator != null) {
                this.checkAnimator.cancel();
            }
            if (animate) {
                this.checkAnimator = ObjectAnimator.ofFloat(this, "checkedState", this.checked ? 1.0f : 0.0f);
                this.checkAnimator.setDuration(200L);
                this.checkAnimator.start();
                return;
            }
            setCheckedState(this.checked ? 1.0f : 0.0f);
        }

        public void setCheckedState(float state) {
            this.checkedState = state;
            invalidate();
        }

        public float getCheckedState() {
            return this.checkedState;
        }

        @Override // android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            updateCheckedState(false);
        }

        @Override // android.view.View
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(62.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(62.0f), 1073741824));
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float radius = AndroidUtilities.dp(20.0f);
            float cx = getMeasuredWidth() * 0.5f;
            float cy = getMeasuredHeight() * 0.5f;
            this.paint.setColor(this.currentAccent.accentColor);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(AndroidUtilities.dp(3.0f));
            this.paint.setAlpha(Math.round(this.checkedState * 255.0f));
            canvas.drawCircle(cx, cy, radius - (this.paint.getStrokeWidth() * 0.5f), this.paint);
            this.paint.setAlpha(255);
            this.paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(cx, cy, radius - (AndroidUtilities.dp(5.0f) * this.checkedState), this.paint);
            if (this.checkedState != 0.0f) {
                this.paint.setColor(-1);
                this.paint.setAlpha(Math.round(this.checkedState * 255.0f));
                canvas.drawCircle(cx, cy, AndroidUtilities.dp(2.0f), this.paint);
                canvas.drawCircle(cx - (AndroidUtilities.dp(7.0f) * this.checkedState), cy, AndroidUtilities.dp(2.0f), this.paint);
                canvas.drawCircle((AndroidUtilities.dp(7.0f) * this.checkedState) + cx, cy, AndroidUtilities.dp(2.0f), this.paint);
            }
            if (this.currentAccent.myMessagesAccentColor != 0 && this.checkedState != 1.0f) {
                this.paint.setColor(this.currentAccent.myMessagesAccentColor);
                canvas.drawCircle(cx, cy, AndroidUtilities.dp(8.0f) * (1.0f - this.checkedState), this.paint);
            }
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            info.setText(LocaleController.getString("ColorPickerMainColor", R.string.ColorPickerMainColor));
            info.setClassName(Button.class.getName());
            info.setChecked(this.checked);
            info.setCheckable(true);
            info.setEnabled(true);
        }
    }

    private static class InnerCustomAccentView extends View {
        private int[] colors;
        private final Paint paint;

        InnerCustomAccentView(Context context) {
            super(context);
            this.paint = new Paint(1);
            this.colors = new int[7];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setTheme(Theme.ThemeInfo themeInfo) {
            if (themeInfo.defaultAccentCount >= 8) {
                this.colors = new int[]{themeInfo.getAccentColor(6), themeInfo.getAccentColor(4), themeInfo.getAccentColor(7), themeInfo.getAccentColor(2), themeInfo.getAccentColor(0), themeInfo.getAccentColor(5), themeInfo.getAccentColor(3)};
            } else {
                this.colors = new int[7];
            }
        }

        @Override // android.view.View
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(62.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(62.0f), 1073741824));
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float centerX = getMeasuredWidth() * 0.5f;
            float centerY = getMeasuredHeight() * 0.5f;
            float radSmall = AndroidUtilities.dp(5.0f);
            float radRing = AndroidUtilities.dp(20.0f) - radSmall;
            this.paint.setStyle(Paint.Style.FILL);
            this.paint.setColor(this.colors[0]);
            canvas.drawCircle(centerX, centerY, radSmall, this.paint);
            double angle = 0.0d;
            for (int a = 0; a < 6; a++) {
                float cx = (((float) Math.sin(angle)) * radRing) + centerX;
                float cy = centerY - (((float) Math.cos(angle)) * radRing);
                this.paint.setColor(this.colors[a + 1]);
                canvas.drawCircle(cx, cy, radSmall, this.paint);
                angle += 1.0471975511965976d;
            }
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            info.setText(LocaleController.getString("ColorPickerMainColor", R.string.ColorPickerMainColor));
            info.setClassName(Button.class.getName());
            info.setEnabled(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ThemeAccentsListAdapter extends RecyclerListView.SelectionAdapter {
        private Theme.ThemeInfo currentTheme;
        private Context mContext;
        private ArrayList<Theme.ThemeAccent> themeAccents;

        ThemeAccentsListAdapter(Context context) {
            this.mContext = context;
            notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            this.currentTheme = ThemeActivity.this.currentType == 1 ? Theme.getCurrentNightTheme() : Theme.getCurrentTheme();
            this.themeAccents = new ArrayList<>(this.currentTheme.themeAccents);
            super.notifyDataSetChanged();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int position) {
            return position == getItemCount() - 1 ? 1 : 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0:
                    return new RecyclerListView.Holder(new InnerAccentView(this.mContext));
                default:
                    return new RecyclerListView.Holder(new InnerCustomAccentView(this.mContext));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case 0:
                    InnerAccentView view = (InnerAccentView) holder.itemView;
                    view.setThemeAndColor(this.currentTheme, this.themeAccents.get(position));
                    break;
                case 1:
                    InnerCustomAccentView view2 = (InnerCustomAccentView) holder.itemView;
                    view2.setTheme(this.currentTheme);
                    break;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (this.themeAccents.isEmpty()) {
                return 0;
            }
            return this.themeAccents.size() + 1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int findCurrentAccent() {
            return this.themeAccents.indexOf(this.currentTheme.getAccent(false));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ListAdapter extends RecyclerListView.SelectionAdapter {
        private static final int TYPE_APP_ICON = 20;
        private static final int TYPE_BRIGHTNESS = 6;
        private static final int TYPE_BUBBLE_RADIUS = 13;
        private static final int TYPE_CHAT_LIST = 9;
        private static final int TYPE_CHOOSE_COLOR = 21;
        private static final int TYPE_DEFAULT_THEMES_PREVIEW = 17;
        private static final int TYPE_HEADER = 5;
        private static final int TYPE_NIGHT_THEME = 10;
        private static final int TYPE_SAVE_TO_GALLERY = 19;
        private static final int TYPE_SHADOW = 3;
        private static final int TYPE_SWIPE_GESTURE = 15;
        private static final int TYPE_TEXT_CHECK = 7;
        private static final int TYPE_TEXT_INFO_PRIVACY = 2;
        private static final int TYPE_TEXT_PREFERENCE = 14;
        private static final int TYPE_TEXT_SETTING = 1;
        private static final int TYPE_TEXT_SIZE = 8;
        private static final int TYPE_THEME_ACCENT_LIST = 12;
        private static final int TYPE_THEME_LIST = 11;
        private static final int TYPE_THEME_PREVIEW = 16;
        private static final int TYPE_THEME_TYPE = 4;
        private boolean first = true;
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return ThemeActivity.this.rowCount;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int type = holder.getItemViewType();
            return type == 0 || type == 1 || type == 4 || type == 7 || type == 10 || type == 11 || type == 12 || type == 14 || type == 18 || type == 20 || type == 21;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void showOptionsForTheme(final Theme.ThemeInfo themeInfo) {
            boolean hasDelete;
            CharSequence[] items;
            int[] icons;
            if (ThemeActivity.this.getParentActivity() != null) {
                if ((themeInfo.info != null && !themeInfo.themeLoaded) || ThemeActivity.this.currentType == 1) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(ThemeActivity.this.getParentActivity());
                if (themeInfo.pathToFile == null) {
                    hasDelete = false;
                    items = new CharSequence[]{null, LocaleController.getString("ExportTheme", R.string.ExportTheme)};
                    icons = new int[]{0, R.drawable.msg_shareout};
                } else {
                    hasDelete = themeInfo.info == null || !themeInfo.info.isDefault;
                    CharSequence[] charSequenceArr = new CharSequence[5];
                    charSequenceArr[0] = LocaleController.getString("ShareFile", R.string.ShareFile);
                    charSequenceArr[1] = LocaleController.getString("ExportTheme", R.string.ExportTheme);
                    charSequenceArr[2] = (themeInfo.info == null || (!themeInfo.info.isDefault && themeInfo.info.creator)) ? LocaleController.getString("Edit", R.string.Edit) : null;
                    charSequenceArr[3] = (themeInfo.info == null || !themeInfo.info.creator) ? null : LocaleController.getString("ThemeSetUrl", R.string.ThemeSetUrl);
                    charSequenceArr[4] = hasDelete ? LocaleController.getString("Delete", R.string.Delete) : null;
                    items = charSequenceArr;
                    icons = new int[]{R.drawable.msg_share, R.drawable.msg_shareout, R.drawable.msg_edit, R.drawable.msg_link, R.drawable.msg_delete};
                }
                builder.setItems(items, icons, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$ListAdapter$$ExternalSyntheticLambda5
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        m12319x1cb31ddc(themeInfo, dialogInterface, i);
                    }
                });
                AlertDialog alertDialog = builder.create();
                ThemeActivity.this.showDialog(alertDialog);
                if (hasDelete) {
                    alertDialog.setItemColor(alertDialog.getItemsCount() - 1, Theme.getColor(Theme.key_text_RedBold), Theme.getColor(Theme.key_text_RedRegular));
                }
            }
        }

        /* JADX INFO: renamed from: lambda$showOptionsForTheme$1$org-telegram-ui-ThemeActivity$ListAdapter, reason: not valid java name */
        void m12319x1cb31ddc(final Theme.ThemeInfo themeInfo, DialogInterface dialogInterface, int which) {
            if (ThemeActivity.this.getParentActivity() == null) {
                return;
            }
            if (which == 0) {
                if (themeInfo.info == null) {
                    MessagesController.getInstance(themeInfo.account).saveThemeToServer(themeInfo, themeInfo.getAccent(false));
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needShareTheme, themeInfo, themeInfo.getAccent(false));
                    return;
                } else {
                    String link = "https://" + ThemeActivity.this.getMessagesController().linkPrefix + "/addtheme/" + themeInfo.info.slug;
                    ThemeActivity.this.showDialog(new ShareAlert(ThemeActivity.this.getParentActivity(), null, link, false, link, false));
                    return;
                }
            }
            if (which == 1) {
                if (themeInfo.pathToFile == null && themeInfo.assetName == null) {
                    return;
                }
                String shareName = themeInfo.name;
                if (!shareName.endsWith(".attheme")) {
                    shareName = shareName + ".attheme";
                }
                java.io.File finalFile = new java.io.File(org.telegram.messenger.FileLoader.getDirectory(4), org.telegram.messenger.FileLoader.fixFileName(shareName));
                java.io.File currentFile = themeInfo.assetName != null ? Theme.getAssetFile(themeInfo.assetName) : new java.io.File(themeInfo.pathToFile);
                if (!AndroidUtilities.copyFile(currentFile, finalFile)) {
                    return;
                }
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/xml");
                if (Build.VERSION.SDK_INT >= 24) {
                    try {
                        intent.putExtra("android.intent.extra.STREAM", androidx.core.content.FileProvider.getUriForFile(ThemeActivity.this.getParentActivity(), ApplicationLoader.getApplicationId() + ".provider", finalFile));
                        intent.setFlags(1);
                    } catch (Exception e) {
                        intent.putExtra("android.intent.extra.STREAM", android.net.Uri.fromFile(finalFile));
                    }
                } else {
                    intent.putExtra("android.intent.extra.STREAM", android.net.Uri.fromFile(finalFile));
                }
                try {
                    ThemeActivity.this.getParentActivity().startActivityForResult(Intent.createChooser(intent, LocaleController.getString("ShareFile", R.string.ShareFile)), 500);
                    return;
                } catch (Exception e2) {
                    return;
                }
            }
            if (which == 2) {
                if (ThemeActivity.this.parentLayout != null) {
                    Theme.applyTheme(themeInfo);
                    ThemeActivity.this.parentLayout.rebuildAllFragmentViews(true, true);
                    new org.telegram.ui.Components.ThemeEditorView().show(ThemeActivity.this.getParentActivity(), themeInfo);
                    return;
                }
                return;
            }
            if (which == 3) {
                ThemeActivity.this.presentFragment(new ThemeSetUrlActivity(themeInfo, null, false));
                return;
            }
            AlertDialog.Builder builder1 = new AlertDialog.Builder(ThemeActivity.this.getParentActivity());
            builder1.setTitle(LocaleController.getString("DeleteThemeTitle", R.string.DeleteThemeTitle));
            builder1.setMessage(LocaleController.getString("DeleteThemeAlert", R.string.DeleteThemeAlert));
            builder1.setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new AlertDialog.OnButtonClickListener() {
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public void onClick(AlertDialog alertDialog, int i) {
                    m12318x3771af1b(themeInfo, alertDialog, i);
                }
            });
            builder1.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            AlertDialog alertDialog2 = builder1.create();
            ThemeActivity.this.showDialog(alertDialog2);
            TextView button = (TextView) alertDialog2.getButton(-1);
            if (button != null) {
                button.setTextColor(Theme.getColor(Theme.key_text_RedBold));
            }
        }

        /* JADX INFO: renamed from: lambda$showOptionsForTheme$0$org-telegram-ui-ThemeActivity$ListAdapter, reason: not valid java name */
        /* synthetic */ void m12318x3771af1b(Theme.ThemeInfo themeInfo, AlertDialog dialogInterface, int i) {
            MessagesController.getInstance(themeInfo.account).saveTheme(themeInfo, null, themeInfo == Theme.getCurrentNightTheme(), true);
            if (Theme.deleteTheme(themeInfo)) {
                ThemeActivity.this.parentLayout.rebuildAllFragmentViews(true, true);
            }
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.themeListUpdated, new Object[0]);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            int i = 0;
            switch (viewType) {
                case 1:
                    view = new TextSettingsCell(this.mContext);
                    break;
                case 2:
                    view = new TextInfoPrivacyCell(this.mContext);
                    break;
                case 3:
                    view = new ShadowSectionCell(this.mContext);
                    break;
                case 4:
                    view = new ThemeTypeCell(this.mContext);
                    break;
                case 5:
                    view = new HeaderCell(this.mContext);
                    break;
                case 6:
                    view = new BrightnessControlCell(this.mContext, i) { // from class: org.telegram.ui.ThemeActivity.ListAdapter.1
                        @Override // org.telegram.ui.Cells.BrightnessControlCell
                        protected void didChangedValue(float value) {
                            int oldValue = (int) (Theme.autoNightBrighnessThreshold * 100.0f);
                            int newValue = (int) (value * 100.0f);
                            Theme.autoNightBrighnessThreshold = value;
                            if (oldValue != newValue) {
                                RecyclerListView.Holder holder = (RecyclerListView.Holder) ThemeActivity.this.listView.findViewHolderForAdapterPosition(ThemeActivity.this.automaticBrightnessInfoRow);
                                if (holder != null) {
                                    TextInfoPrivacyCell cell = (TextInfoPrivacyCell) holder.itemView;
                                    cell.setText(LocaleController.formatString("AutoNightBrightnessInfo", R.string.AutoNightBrightnessInfo, Integer.valueOf((int) (Theme.autoNightBrighnessThreshold * 100.0f))));
                                }
                                Theme.checkAutoNightThemeConditions(true);
                            }
                        }
                    };
                    break;
                case 7:
                    view = new TextCheckCell(this.mContext);
                    break;
                case 8:
                    view = ThemeActivity.this.new TextSizeCell(this.mContext);
                    break;
                case 9:
                    view = new ChatListCell(this.mContext) { // from class: org.telegram.ui.ThemeActivity.ListAdapter.2
                        @Override // org.telegram.ui.Cells.ChatListCell
                        protected void didSelectChatType(boolean threeLines) {
                            SharedConfig.setUseThreeLinesLayout(threeLines);
                        }
                    };
                    break;
                case 10:
                    view = new NotificationsCheckCell(this.mContext, 21, 60, true);
                    break;
                case 11:
                    this.first = true;
                    ThemeActivity.this.themesHorizontalListCell = new ThemesHorizontalListCell(this.mContext, ThemeActivity.this, ThemeActivity.this.currentType, ThemeActivity.this.defaultThemes, ThemeActivity.this.darkThemes) { // from class: org.telegram.ui.ThemeActivity.ListAdapter.3
                        @Override // org.telegram.ui.Cells.ThemesHorizontalListCell
                        protected void showOptionsForTheme(Theme.ThemeInfo themeInfo) {
                            ThemeActivity.this.listAdapter.showOptionsForTheme(themeInfo);
                        }

                        @Override // org.telegram.ui.Cells.ThemesHorizontalListCell
                        protected void updateRows() {
                            ThemeActivity.this.updateRows(false);
                        }
                    };
                    ThemeActivity.this.themesHorizontalListCell.setDrawDivider(ThemeActivity.this.hasThemeAccents);
                    ThemeActivity.this.themesHorizontalListCell.setFocusable(false);
                    view = ThemeActivity.this.themesHorizontalListCell;
                    view.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(148.0f)));
                    break;
                case 12:
                    final RecyclerListView accentsListView = new TintRecyclerListView(this.mContext) { // from class: org.telegram.ui.ThemeActivity.ListAdapter.4
                        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                        public boolean onInterceptTouchEvent(MotionEvent e) {
                            if (getParent() != null && getParent().getParent() != null) {
                                getParent().getParent().requestDisallowInterceptTouchEvent(canScrollHorizontally(-1));
                            }
                            return super.onInterceptTouchEvent(e);
                        }
                    };
                    accentsListView.setFocusable(false);
                    accentsListView.setItemAnimator(null);
                    accentsListView.setLayoutAnimation(null);
                    accentsListView.setPadding(AndroidUtilities.dp(11.0f), 0, AndroidUtilities.dp(11.0f), 0);
                    accentsListView.setClipToPadding(false);
                    LinearLayoutManager accentsLayoutManager = new LinearLayoutManager(this.mContext);
                    accentsLayoutManager.setOrientation(0);
                    accentsListView.setLayoutManager(accentsLayoutManager);
                    final ThemeAccentsListAdapter accentsAdapter = ThemeActivity.this.new ThemeAccentsListAdapter(this.mContext);
                    accentsListView.setAdapter(accentsAdapter);
                    accentsListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.ThemeActivity$ListAdapter$$ExternalSyntheticLambda0
                        @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                        public final void onItemClick(View view2, int i2) {
                            m12314x25c35c2(accentsAdapter, accentsListView, view2, i2);
                        }
                    });
                    accentsListView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.ThemeActivity$ListAdapter$$ExternalSyntheticLambda1
                        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
                        public final boolean onItemClick(View view2, int i2) {
                            return m12317xb2208205(accentsAdapter, view2, i2);
                        }
                    });
                    accentsListView.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(62.0f)));
                    view = accentsListView;
                    break;
                case 13:
                    view = ThemeActivity.this.new BubbleRadiusCell(this.mContext);
                    break;
                case 14:
                case 18:
                default:
                    view = new TextCell(this.mContext);
                    break;
                case 15:
                    view = new SwipeGestureSettingsView(this.mContext, ThemeActivity.this.currentAccount);
                    break;
                case 16:
                    ThemePreviewMessagesCell messagesCell = new ThemePreviewMessagesCell(this.mContext, ThemeActivity.this.parentLayout, i) { // from class: org.telegram.ui.ThemeActivity.ListAdapter.5
                        @Override // org.telegram.ui.Cells.ThemePreviewMessagesCell, android.view.ViewGroup
                        public boolean onInterceptTouchEvent(MotionEvent e) {
                            if (getParent() != null && getParent().getParent() != null) {
                                getParent().getParent().requestDisallowInterceptTouchEvent(canScrollHorizontally(-1));
                            }
                            return super.onInterceptTouchEvent(e);
                        }
                    };
                    messagesCell.setImportantForAccessibility(4);
                    view = messagesCell;
                    break;
                case 17:
                    DefaultThemesPreviewCell cell = new DefaultThemesPreviewCell(this.mContext, ThemeActivity.this, ThemeActivity.this.currentType);
                    view = cell;
                    cell.setFocusable(false);
                    view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
                    break;
                case 19:
                    view = new RadioButtonCell(this.mContext);
                    break;
                case 20:
                    view = new AppIconsSelectorCell(this.mContext, ThemeActivity.this, ThemeActivity.this.currentAccount);
                    break;
                case 21:
                    view = new PeerColorActivity.ChangeNameColorCell(ThemeActivity.this.currentAccount, 0L, this.mContext, ThemeActivity.this.getResourceProvider());
                    break;
            }
            return new RecyclerListView.Holder(view);
        }

        /* JADX INFO: renamed from: lambda$onCreateViewHolder$2$org-telegram-ui-ThemeActivity$ListAdapter, reason: not valid java name */
        /* synthetic */ void m12314x25c35c2(ThemeAccentsListAdapter accentsAdapter, RecyclerListView accentsListView, View view1, int position) {
            Theme.ThemeInfo currentTheme = ThemeActivity.this.currentType == 1 ? Theme.getCurrentNightTheme() : Theme.getCurrentTheme();
            if (position != accentsAdapter.getItemCount() - 1) {
                Theme.ThemeAccent accent = (Theme.ThemeAccent) accentsAdapter.themeAccents.get(position);
                if (!TextUtils.isEmpty(accent.patternSlug) && accent.id != Theme.DEFALT_THEME_ACCENT_ID) {
                    Theme.PatternsLoader.createLoader(false);
                }
                if (currentTheme.currentAccentId != accent.id) {
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, currentTheme, Boolean.valueOf(ThemeActivity.this.currentType == 1), null, Integer.valueOf(accent.id));
                    EmojiThemes.saveCustomTheme(currentTheme, accent.id);
                    Theme.turnOffAutoNight(ThemeActivity.this);
                } else {
                    ThemeActivity.this.presentFragment(new ThemePreviewActivity(currentTheme, false, 1, accent.id >= 100, ThemeActivity.this.currentType == 1));
                }
            } else {
                ThemeActivity.this.presentFragment(new ThemePreviewActivity(currentTheme, false, 1, false, ThemeActivity.this.currentType == 1));
            }
            int left = view1.getLeft();
            int right = view1.getRight();
            int extra = AndroidUtilities.dp(52.0f);
            if (left - extra < 0) {
                accentsListView.smoothScrollBy(left - extra, 0);
            } else if (right + extra > accentsListView.getMeasuredWidth()) {
                accentsListView.smoothScrollBy((right + extra) - accentsListView.getMeasuredWidth(), 0);
            }
            int count = accentsListView.getChildCount();
            for (int a = 0; a < count; a++) {
                View child = accentsListView.getChildAt(a);
                if (child instanceof InnerAccentView) {
                    ((InnerAccentView) child).updateCheckedState(true);
                }
            }
        }

        /* JADX INFO: renamed from: lambda$onCreateViewHolder$5$org-telegram-ui-ThemeActivity$ListAdapter, reason: not valid java name */
        /* synthetic */ boolean m12317xb2208205(final ThemeAccentsListAdapter accentsAdapter, View view12, int position) {
            if (position >= 0 && position < accentsAdapter.themeAccents.size()) {
                final Theme.ThemeAccent accent = (Theme.ThemeAccent) accentsAdapter.themeAccents.get(position);
                if (accent.id < 100 || accent.isDefault) {
                    return false;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(ThemeActivity.this.getParentActivity());
                CharSequence[] items = new CharSequence[4];
                items[0] = LocaleController.getString("OpenInEditor", R.string.OpenInEditor);
                items[1] = LocaleController.getString("ShareTheme", R.string.ShareTheme);
                items[2] = (accent.info == null || !accent.info.creator) ? null : LocaleController.getString("ThemeSetUrl", R.string.ThemeSetUrl);
                items[3] = LocaleController.getString("DeleteTheme", R.string.DeleteTheme);
                int[] icons = {R.drawable.msg_edit, R.drawable.msg_share, R.drawable.msg_link, R.drawable.msg_delete};
                builder.setItems(items, icons, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$ListAdapter$$ExternalSyntheticLambda2
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        m12316xccdf1344(accent, accentsAdapter, dialogInterface, i);
                    }
                });
                AlertDialog alertDialog = builder.create();
                ThemeActivity.this.showDialog(alertDialog);
                alertDialog.setItemColor(alertDialog.getItemsCount() - 1, Theme.getColor(Theme.key_text_RedBold), Theme.getColor(Theme.key_text_RedRegular));
                return true;
            }
            return false;
        }

        /* JADX INFO: renamed from: lambda$onCreateViewHolder$4$org-telegram-ui-ThemeActivity$ListAdapter, reason: not valid java name */
        /* synthetic */ void m12316xccdf1344(final Theme.ThemeAccent accent, final ThemeAccentsListAdapter accentsAdapter, DialogInterface dialog, int which) {
            if (ThemeActivity.this.getParentActivity() == null) {
                return;
            }
            if (which == 0) {
                AlertsCreator.createThemeCreateDialog(ThemeActivity.this, which != 1 ? 1 : 2, accent.parentTheme, accent);
                return;
            }
            if (which == 1) {
                if (accent.info == null) {
                    ThemeActivity.this.getMessagesController().saveThemeToServer(accent.parentTheme, accent);
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needShareTheme, accent.parentTheme, accent);
                    return;
                } else {
                    String link = "https://" + ThemeActivity.this.getMessagesController().linkPrefix + "/addtheme/" + accent.info.slug;
                    ThemeActivity.this.showDialog(new ShareAlert(ThemeActivity.this.getParentActivity(), null, link, false, link, false));
                    return;
                }
            }
            if (which == 2) {
                ThemeActivity.this.presentFragment(new ThemeSetUrlActivity(accent.parentTheme, accent, false));
                return;
            }
            if (which != 3 || ThemeActivity.this.getParentActivity() == null) {
                return;
            }
            AlertDialog.Builder builder1 = new AlertDialog.Builder(ThemeActivity.this.getParentActivity());
            builder1.setTitle(LocaleController.getString("DeleteThemeTitle", R.string.DeleteThemeTitle));
            builder1.setMessage(LocaleController.getString("DeleteThemeAlert", R.string.DeleteThemeAlert));
            builder1.setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.ThemeActivity$ListAdapter$$ExternalSyntheticLambda3
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i) {
                    m12315xe79da483(accentsAdapter, accent, alertDialog, i);
                }
            });
            builder1.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            AlertDialog alertDialog = builder1.create();
            ThemeActivity.this.showDialog(alertDialog);
            TextView button = (TextView) alertDialog.getButton(-1);
            if (button != null) {
                button.setTextColor(Theme.getColor(Theme.key_text_RedBold));
            }
        }

        /* JADX INFO: renamed from: lambda$onCreateViewHolder$3$org-telegram-ui-ThemeActivity$ListAdapter, reason: not valid java name */
        /* synthetic */ void m12315xe79da483(ThemeAccentsListAdapter accentsAdapter, Theme.ThemeAccent accent, AlertDialog dialogInterface, int i) {
            if (Theme.deleteThemeAccent(accentsAdapter.currentTheme, accent, true)) {
                Theme.refreshThemeColors();
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, Theme.getActiveTheme(), Boolean.valueOf(ThemeActivity.this.currentType == 1), null, -1);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            String value;
            String value2;
            String value3;
            String type;
            switch (holder.getItemViewType()) {
                case 1:
                    TextSettingsCell cell = (TextSettingsCell) holder.itemView;
                    if (position != ThemeActivity.this.nightThemeRow) {
                        if (position != ThemeActivity.this.scheduleFromRow) {
                            if (position != ThemeActivity.this.scheduleToRow) {
                                if (position != ThemeActivity.this.scheduleUpdateLocationRow) {
                                    if (position != ThemeActivity.this.contactsSortRow) {
                                        if (position != ThemeActivity.this.contactsReimportRow) {
                                            if (position != ThemeActivity.this.distanceRow) {
                                                if (position == ThemeActivity.this.searchEngineRow) {
                                                    cell.setTextAndValue(LocaleController.getString(R.string.SearchEngine), SearchEngine.getCurrent().name, ThemeActivity.this.updateSearchEngine, false);
                                                } else if (position == ThemeActivity.this.bluetoothScoRow) {
                                                    cell.setTextAndValue(LocaleController.getString(R.string.MicrophoneForVoiceMessages), LocaleController.getString(SharedConfig.recordViaSco ? R.string.MicrophoneForVoiceMessagesSco : R.string.MicrophoneForVoiceMessagesBuiltIn), ThemeActivity.this.updateRecordViaSco, false);
                                                    ThemeActivity.this.updateRecordViaSco = false;
                                                }
                                            } else {
                                                if (SharedConfig.distanceSystemType == 0) {
                                                    value = LocaleController.getString("DistanceUnitsAutomatic", R.string.DistanceUnitsAutomatic);
                                                } else if (SharedConfig.distanceSystemType == 1) {
                                                    value = LocaleController.getString("DistanceUnitsKilometers", R.string.DistanceUnitsKilometers);
                                                } else {
                                                    value = LocaleController.getString("DistanceUnitsMiles", R.string.DistanceUnitsMiles);
                                                }
                                                cell.setTextAndValue(LocaleController.getString("DistanceUnits", R.string.DistanceUnits), value, ThemeActivity.this.updateDistance, ThemeActivity.this.otherSectionRow >= 0);
                                                ThemeActivity.this.updateDistance = false;
                                            }
                                        } else {
                                            cell.setText(LocaleController.getString("ImportContacts", R.string.ImportContacts), true);
                                        }
                                    } else {
                                        SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                                        int sort = preferences.getInt("sortContactsBy", 0);
                                        if (sort == 0) {
                                            value2 = LocaleController.getString(Profile.DEFAULT_PROFILE_NAME, R.string.Default);
                                        } else if (sort == 1) {
                                            value2 = LocaleController.getString("FirstName", R.string.SortFirstName);
                                        } else {
                                            value2 = LocaleController.getString("LastName", R.string.SortLastName);
                                        }
                                        cell.setTextAndValue(LocaleController.getString("SortBy", R.string.SortBy), value2, true);
                                    }
                                } else {
                                    cell.setTextAndValue(LocaleController.getString("AutoNightUpdateLocation", R.string.AutoNightUpdateLocation), Theme.autoNightCityName, false);
                                }
                            } else {
                                int currentHour = Theme.autoNightDayEndTime / 60;
                                int currentMinute = Theme.autoNightDayEndTime - (currentHour * 60);
                                cell.setTextAndValue(LocaleController.getString("AutoNightTo", R.string.AutoNightTo), String.format("%02d:%02d", Integer.valueOf(currentHour), Integer.valueOf(currentMinute)), false);
                            }
                        } else {
                            int currentHour2 = Theme.autoNightDayStartTime / 60;
                            int currentMinute2 = Theme.autoNightDayStartTime - (currentHour2 * 60);
                            cell.setTextAndValue(LocaleController.getString("AutoNightFrom", R.string.AutoNightFrom), String.format("%02d:%02d", Integer.valueOf(currentHour2), Integer.valueOf(currentMinute2)), true);
                        }
                    } else if (Theme.selectedAutoNightType == 0 || Theme.getCurrentNightTheme() == null) {
                        cell.setTextAndValue(LocaleController.getString(R.string.AutoNightTheme), LocaleController.getString(R.string.AutoNightThemeOff), false);
                    } else {
                        cell.setTextAndValue(LocaleController.getString(R.string.AutoNightTheme), Theme.getCurrentNightThemeName(), false);
                    }
                    break;
                case 2:
                    TextInfoPrivacyCell cell2 = (TextInfoPrivacyCell) holder.itemView;
                    cell2.setFixedSize(0);
                    if (position == ThemeActivity.this.automaticBrightnessInfoRow) {
                        cell2.setText(LocaleController.formatString("AutoNightBrightnessInfo", R.string.AutoNightBrightnessInfo, Integer.valueOf((int) (Theme.autoNightBrighnessThreshold * 100.0f))));
                    } else if (position == ThemeActivity.this.scheduleLocationInfoRow) {
                        cell2.setText(ThemeActivity.this.getLocationSunString());
                    } else if (position != ThemeActivity.this.swipeGestureInfoRow) {
                        if (position == ThemeActivity.this.liteModeInfoRow) {
                            cell2.setText(LocaleController.getString("LiteModeInfo", R.string.LiteModeInfo));
                        } else {
                            cell2.setFixedSize(12);
                            cell2.setText("");
                        }
                    } else {
                        cell2.setText(LocaleController.getString("ChatListSwipeGestureInfo", R.string.ChatListSwipeGestureInfo));
                    }
                    break;
                case 4:
                    ThemeTypeCell typeCell = (ThemeTypeCell) holder.itemView;
                    if (position != ThemeActivity.this.nightDisabledRow) {
                        if (position != ThemeActivity.this.nightScheduledRow) {
                            if (position == ThemeActivity.this.nightAutomaticRow) {
                                typeCell.setValue(LocaleController.getString("AutoNightAdaptive", R.string.AutoNightAdaptive), Theme.selectedAutoNightType == 2, ThemeActivity.this.nightSystemDefaultRow != -1);
                            } else if (position == ThemeActivity.this.nightSystemDefaultRow) {
                                typeCell.setValue(LocaleController.getString("AutoNightSystemDefault", R.string.AutoNightSystemDefault), Theme.selectedAutoNightType == 3, false);
                            }
                        } else {
                            typeCell.setValue(LocaleController.getString("AutoNightScheduled", R.string.AutoNightScheduled), Theme.selectedAutoNightType == 1, true);
                        }
                    } else {
                        typeCell.setValue(LocaleController.getString("AutoNightDisabled", R.string.AutoNightDisabled), Theme.selectedAutoNightType == 0, true);
                    }
                    break;
                case 5:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position != ThemeActivity.this.scheduleHeaderRow) {
                        if (position != ThemeActivity.this.automaticHeaderRow) {
                            if (position != ThemeActivity.this.preferedHeaderRow) {
                                if (position != ThemeActivity.this.settingsRow) {
                                    if (position == ThemeActivity.this.themeHeaderRow) {
                                        if (ThemeActivity.this.currentType == 3) {
                                            headerCell.setText(LocaleController.getString("BuildMyOwnTheme", R.string.BuildMyOwnTheme));
                                        } else {
                                            headerCell.setText(LocaleController.getString("ColorTheme", R.string.ColorTheme));
                                        }
                                    } else if (position != ThemeActivity.this.textSizeHeaderRow) {
                                        if (position != ThemeActivity.this.chatListHeaderRow) {
                                            if (position != ThemeActivity.this.bubbleRadiusHeaderRow) {
                                                if (position != ThemeActivity.this.swipeGestureHeaderRow) {
                                                    if (position != ThemeActivity.this.selectThemeHeaderRow) {
                                                        if (position != ThemeActivity.this.appIconHeaderRow) {
                                                            if (position != ThemeActivity.this.otherHeaderRow) {
                                                                if (position == ThemeActivity.this.mediaSoundHeaderRow) {
                                                                    headerCell.setText(LocaleController.getString("MediaAndSoundSettings", R.string.MediaAndSoundSettings));
                                                                }
                                                            } else {
                                                                headerCell.setText(LocaleController.getString("OtherSettings", R.string.OtherSettings));
                                                            }
                                                        } else {
                                                            headerCell.setText(LocaleController.getString(R.string.AppIcon));
                                                        }
                                                    } else {
                                                        headerCell.setText(LocaleController.getString("SelectTheme", R.string.SelectTheme));
                                                    }
                                                } else {
                                                    headerCell.setText(LocaleController.getString("ChatListSwipeGesture", R.string.ChatListSwipeGesture));
                                                }
                                            } else {
                                                headerCell.setText(LocaleController.getString("BubbleRadius", R.string.BubbleRadius));
                                            }
                                        } else {
                                            headerCell.setText(LocaleController.getString("ChatList", R.string.ChatList));
                                        }
                                    } else {
                                        headerCell.setText(LocaleController.getString("TextSizeHeader", R.string.TextSizeHeader));
                                    }
                                } else {
                                    headerCell.setText(LocaleController.getString("SETTINGS", R.string.SETTINGS));
                                }
                            } else {
                                headerCell.setText(LocaleController.getString("AutoNightPreferred", R.string.AutoNightPreferred));
                            }
                        } else {
                            headerCell.setText(LocaleController.getString("AutoNightBrightness", R.string.AutoNightBrightness));
                        }
                    } else {
                        headerCell.setText(LocaleController.getString("AutoNightSchedule", R.string.AutoNightSchedule));
                    }
                    break;
                case 6:
                    ((BrightnessControlCell) holder.itemView).setProgress(Theme.autoNightBrighnessThreshold);
                    break;
                case 7:
                    TextCheckCell textCheckCell = (TextCheckCell) holder.itemView;
                    if (position != ThemeActivity.this.scheduleLocationRow) {
                        if (position != ThemeActivity.this.enableAnimationsRow) {
                            if (position != ThemeActivity.this.sendByEnterRow) {
                                if (position != ThemeActivity.this.raiseToSpeakRow) {
                                    if (position != ThemeActivity.this.raiseToListenRow) {
                                        if (position != ThemeActivity.this.nextMediaTapRow) {
                                            if (position != ThemeActivity.this.pauseOnRecordRow) {
                                                if (position != ThemeActivity.this.pauseOnMediaRow) {
                                                    if (position != ThemeActivity.this.directShareRow) {
                                                        if (position != ThemeActivity.this.sensitiveContentRow) {
                                                            if (position == ThemeActivity.this.chatBlurRow) {
                                                                textCheckCell.setTextAndCheck(LocaleController.getString("BlurInChat", R.string.BlurInChat), SharedConfig.chatBlurEnabled(), true);
                                                            }
                                                        } else {
                                                            textCheckCell.setTextAndValueAndCheck(LocaleController.getString(R.string.ShowSensitiveContent), LocaleController.getString(R.string.ShowSensitiveContentInfo), ThemeActivity.this.getMessagesController().showSensitiveContent(), true, true);
                                                        }
                                                    } else {
                                                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("DirectShare", R.string.DirectShare), LocaleController.getString("DirectShareInfo", R.string.DirectShareInfo), SharedConfig.directShare, false, true);
                                                    }
                                                } else {
                                                    textCheckCell.setTextAndCheck(LocaleController.getString(R.string.PauseMusicOnMedia), SharedConfig.pauseMusicOnMedia, true);
                                                }
                                            } else {
                                                textCheckCell.setTextAndValueAndCheck(LocaleController.getString(R.string.PauseMusicOnRecord), LocaleController.getString("PauseMusicOnRecordInfo", R.string.PauseMusicOnRecordInfo), SharedConfig.pauseMusicOnRecord, true, true);
                                            }
                                        } else {
                                            textCheckCell.setTextAndValueAndCheck(LocaleController.getString("NextMediaTap", R.string.NextMediaTap), LocaleController.getString("NextMediaTapInfo", R.string.NextMediaTapInfo), SharedConfig.nextMediaTap, true, true);
                                        }
                                    } else {
                                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("RaiseToListen", R.string.RaiseToListen), LocaleController.getString("RaiseToListenInfo", R.string.RaiseToListenInfo), SharedConfig.raiseToListen, true, true);
                                    }
                                } else {
                                    textCheckCell.setTextAndValueAndCheck(LocaleController.getString("RaiseToSpeak", R.string.RaiseToSpeak), LocaleController.getString("RaiseToSpeakInfo", R.string.RaiseToSpeakInfo), SharedConfig.raiseToSpeak, true, true);
                                }
                            } else {
                                SharedPreferences preferences2 = MessagesController.getGlobalMainSettings();
                                textCheckCell.setTextAndCheck(LocaleController.getString("SendByEnter", R.string.SendByEnter), preferences2.getBoolean("send_by_enter", false), true);
                            }
                        } else {
                            SharedPreferences preferences3 = MessagesController.getGlobalMainSettings();
                            textCheckCell.setTextAndCheck(LocaleController.getString("EnableAnimations", R.string.EnableAnimations), preferences3.getBoolean("view_animations", true), true);
                        }
                    } else {
                        textCheckCell.setTextAndCheck(LocaleController.getString("AutoNightLocation", R.string.AutoNightLocation), Theme.autoNightScheduleByLocation, true);
                    }
                    break;
                case 10:
                    NotificationsCheckCell checkCell = (NotificationsCheckCell) holder.itemView;
                    if (position != ThemeActivity.this.nightThemeRow) {
                        if (position == ThemeActivity.this.browserRow) {
                            checkCell.setTextAndValueAndIconAndCheck(LocaleController.getString(R.string.InappBrowser), LocaleController.getString(R.string.InappBrowserInfo), R.drawable.msg2_language, ThemeActivity.this.getMessagesController().isWebBrowserInAppEnabled(), 0, false, true);
                        }
                    } else {
                        boolean enabled = Theme.selectedAutoNightType != 0;
                        String value4 = enabled ? Theme.getCurrentNightThemeName() : LocaleController.getString("AutoNightThemeOff", R.string.AutoNightThemeOff);
                        if (!enabled) {
                            value3 = value4;
                        } else {
                            if (Theme.selectedAutoNightType == 1) {
                                type = LocaleController.getString("AutoNightScheduled", R.string.AutoNightScheduled);
                            } else if (Theme.selectedAutoNightType == 3) {
                                type = LocaleController.getString("AutoNightSystemDefault", R.string.AutoNightSystemDefault);
                            } else {
                                type = LocaleController.getString("AutoNightAdaptive", R.string.AutoNightAdaptive);
                            }
                            value3 = type + " " + value4;
                        }
                        checkCell.setTextAndValueAndIconAndCheck(LocaleController.getString("AutoNightTheme", R.string.AutoNightTheme), value3, R.drawable.menu_night_mode_24, enabled, 0, false, true);
                    }
                    break;
                case 11:
                    if (this.first) {
                        ThemeActivity.this.themesHorizontalListCell.scrollToCurrentTheme(ThemeActivity.this.listView.getMeasuredWidth(), false);
                        this.first = false;
                    }
                    break;
                case 12:
                    RecyclerListView accentsList = (RecyclerListView) holder.itemView;
                    ThemeAccentsListAdapter adapter = (ThemeAccentsListAdapter) accentsList.getAdapter();
                    adapter.notifyDataSetChanged();
                    int pos = adapter.findCurrentAccent();
                    if (pos == -1) {
                        pos = adapter.getItemCount() - 1;
                    }
                    if (pos != -1) {
                        ((LinearLayoutManager) accentsList.getLayoutManager()).scrollToPositionWithOffset(pos, (ThemeActivity.this.listView.getMeasuredWidth() / 2) - AndroidUtilities.dp(42.0f));
                    }
                    break;
                case 14:
                    TextCell cell3 = (TextCell) holder.itemView;
                    cell3.heightDp = 48;
                    if (position != ThemeActivity.this.backgroundRow) {
                        if (position != ThemeActivity.this.editThemeRow) {
                            if (position != ThemeActivity.this.createNewThemeRow) {
                                if (position != ThemeActivity.this.liteModeRow) {
                                    if (position == ThemeActivity.this.stickersRow) {
                                        cell3.setColors(Theme.key_dialogIcon, Theme.key_windowBackgroundWhiteBlackText);
                                        cell3.setTextAndIcon((CharSequence) LocaleController.getString(R.string.StickersName), R.drawable.msg2_sticker, false);
                                        cell3.setSubtitle(LocaleController.getString(R.string.StickersNameInfo2));
                                        cell3.offsetFromImage = 64;
                                        cell3.heightDp = 60;
                                        cell3.imageLeft = 20;
                                    }
                                } else {
                                    cell3.setColors(Theme.key_dialogIcon, Theme.key_windowBackgroundWhiteBlackText);
                                    cell3.setTextAndIcon((CharSequence) LocaleController.getString(R.string.LiteMode), R.drawable.msg2_animations, true);
                                    cell3.setSubtitle(LocaleController.getString(R.string.LiteModeInfo));
                                    cell3.heightDp = 60;
                                    cell3.offsetFromImage = 64;
                                    cell3.imageLeft = 20;
                                }
                            } else {
                                cell3.setSubtitle(null);
                                cell3.setColors(Theme.key_windowBackgroundWhiteBlueText4, Theme.key_windowBackgroundWhiteBlueText4);
                                cell3.setTextAndIcon((CharSequence) LocaleController.getString(R.string.CreateNewTheme), R.drawable.msg_colors, false);
                            }
                        } else {
                            cell3.setSubtitle(null);
                            cell3.setColors(Theme.key_windowBackgroundWhiteBlueText4, Theme.key_windowBackgroundWhiteBlueText4);
                            cell3.setTextAndIcon((CharSequence) LocaleController.getString(R.string.EditCurrentTheme), R.drawable.msg_theme, true);
                        }
                    } else {
                        cell3.setSubtitle(null);
                        cell3.setColors(Theme.key_windowBackgroundWhiteBlueText4, Theme.key_windowBackgroundWhiteBlueText4);
                        cell3.setTextAndIcon(LocaleController.getString(R.string.ChangeChatBackground), R.drawable.msg_background, ThemeActivity.this.changeUserColor >= 0);
                    }
                    break;
                case 17:
                    ((DefaultThemesPreviewCell) holder.itemView).updateDayNightMode();
                    break;
                case 19:
                    RadioButtonCell radioCell = (RadioButtonCell) holder.itemView;
                    if (position == ThemeActivity.this.saveToGalleryOption1Row) {
                        radioCell.setTextAndValue("save media only from peer chats", "", true, false);
                    } else {
                        radioCell.setTextAndValue("save media from all chats", "", true, false);
                    }
                    break;
                case 21:
                    ((PeerColorActivity.ChangeNameColorCell) holder.itemView).set(ThemeActivity.this.getUserConfig().getCurrentUser());
                    break;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            int type = holder.getItemViewType();
            if (type == 4) {
                ((ThemeTypeCell) holder.itemView).setTypeChecked(holder.getAdapterPosition() == Theme.selectedAutoNightType);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int position) {
            if (position == ThemeActivity.this.scheduleFromRow || position == ThemeActivity.this.distanceRow || position == ThemeActivity.this.scheduleToRow || position == ThemeActivity.this.scheduleUpdateLocationRow || position == ThemeActivity.this.contactsReimportRow || position == ThemeActivity.this.contactsSortRow || position == ThemeActivity.this.bluetoothScoRow || position == ThemeActivity.this.searchEngineRow) {
                return 1;
            }
            if (position != ThemeActivity.this.automaticBrightnessInfoRow && position != ThemeActivity.this.scheduleLocationInfoRow && position != ThemeActivity.this.swipeGestureInfoRow && position != ThemeActivity.this.stickersInfoRow && position != ThemeActivity.this.liteModeInfoRow) {
                if (position != ThemeActivity.this.themeInfoRow && position != ThemeActivity.this.nightTypeInfoRow && position != ThemeActivity.this.scheduleFromToInfoRow && position != ThemeActivity.this.settings2Row && position != ThemeActivity.this.newThemeInfoRow && position != ThemeActivity.this.chatListInfoRow && position != ThemeActivity.this.bubbleRadiusInfoRow && position != ThemeActivity.this.saveToGallerySectionRow && position != ThemeActivity.this.appIconShadowRow && position != ThemeActivity.this.lastShadowRow && position != ThemeActivity.this.stickersSectionRow && position != ThemeActivity.this.mediaSoundSectionRow && position != ThemeActivity.this.otherSectionRow) {
                    if (position != ThemeActivity.this.nightDisabledRow && position != ThemeActivity.this.nightScheduledRow && position != ThemeActivity.this.nightAutomaticRow && position != ThemeActivity.this.nightSystemDefaultRow) {
                        if (position != ThemeActivity.this.scheduleHeaderRow && position != ThemeActivity.this.automaticHeaderRow && position != ThemeActivity.this.preferedHeaderRow && position != ThemeActivity.this.settingsRow && position != ThemeActivity.this.themeHeaderRow && position != ThemeActivity.this.textSizeHeaderRow && position != ThemeActivity.this.chatListHeaderRow && position != ThemeActivity.this.bubbleRadiusHeaderRow && position != ThemeActivity.this.swipeGestureHeaderRow && position != ThemeActivity.this.selectThemeHeaderRow && position != ThemeActivity.this.appIconHeaderRow && position != ThemeActivity.this.mediaSoundHeaderRow && position != ThemeActivity.this.otherHeaderRow) {
                            if (position != ThemeActivity.this.automaticBrightnessRow) {
                                if (position != ThemeActivity.this.scheduleLocationRow && position != ThemeActivity.this.sendByEnterRow && position != ThemeActivity.this.raiseToSpeakRow && position != ThemeActivity.this.raiseToListenRow && position != ThemeActivity.this.pauseOnRecordRow && position != ThemeActivity.this.directShareRow && position != ThemeActivity.this.chatBlurRow && position != ThemeActivity.this.pauseOnMediaRow && position != ThemeActivity.this.nextMediaTapRow && position != ThemeActivity.this.sensitiveContentRow) {
                                    if (position != ThemeActivity.this.textSizeRow) {
                                        if (position != ThemeActivity.this.chatListRow) {
                                            if (position != ThemeActivity.this.nightThemeRow && position != ThemeActivity.this.browserRow) {
                                                if (position != ThemeActivity.this.themeListRow) {
                                                    if (position != ThemeActivity.this.themeAccentListRow) {
                                                        if (position != ThemeActivity.this.bubbleRadiusRow) {
                                                            if (position != ThemeActivity.this.backgroundRow && position != ThemeActivity.this.editThemeRow && position != ThemeActivity.this.createNewThemeRow && position != ThemeActivity.this.liteModeRow && position != ThemeActivity.this.stickersRow) {
                                                                if (position != ThemeActivity.this.swipeGestureRow) {
                                                                    if (position != ThemeActivity.this.themePreviewRow) {
                                                                        if (position != ThemeActivity.this.themeListRow2) {
                                                                            if (position != ThemeActivity.this.saveToGalleryOption1Row && position != ThemeActivity.this.saveToGalleryOption2Row) {
                                                                                if (position == ThemeActivity.this.appIconSelectorRow) {
                                                                                    return 20;
                                                                                }
                                                                                return position == ThemeActivity.this.changeUserColor ? 21 : 1;
                                                                            }
                                                                            return 19;
                                                                        }
                                                                        return 17;
                                                                    }
                                                                    return 16;
                                                                }
                                                                return 15;
                                                            }
                                                            return 14;
                                                        }
                                                        return 13;
                                                    }
                                                    return 12;
                                                }
                                                return 11;
                                            }
                                            return 10;
                                        }
                                        return 9;
                                    }
                                    return 8;
                                }
                                return 7;
                            }
                            return 6;
                        }
                        return 5;
                    }
                    return 4;
                }
                return 3;
            }
            return 2;
        }
    }

    private static abstract class TintRecyclerListView extends RecyclerListView {
        TintRecyclerListView(Context context) {
            super(context);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> themeDescriptions = new ArrayList<>();
        themeDescriptions.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, TextCheckCell.class, HeaderCell.class, BrightnessControlCell.class, ThemeTypeCell.class, TextSizeCell.class, BubbleRadiusCell.class, ChatListCell.class, NotificationsCheckCell.class, ThemesHorizontalListCell.class, TintRecyclerListView.class, TextCell.class, PeerColorActivity.ChangeNameColorCell.class, SwipeGestureSettingsView.class, DefaultThemesPreviewCell.class, AppIconsSelectorCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        themeDescriptions.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        themeDescriptions.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault));
        themeDescriptions.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        themeDescriptions.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        themeDescriptions.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        themeDescriptions.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, Theme.key_actionBarDefaultSubmenuBackground));
        themeDescriptions.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, Theme.key_actionBarDefaultSubmenuItem));
        themeDescriptions.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_actionBarDefaultSubmenuItemIcon));
        themeDescriptions.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteValueText));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueHeader));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueText4));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueText4));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrack));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrackChecked));
        themeDescriptions.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{BrightnessControlCell.class}, new String[]{"leftImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayIcon));
        themeDescriptions.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{BrightnessControlCell.class}, new String[]{"rightImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayIcon));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{BrightnessControlCell.class}, new String[]{"seekBarView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_player_progressBackground));
        themeDescriptions.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_PROGRESSBAR, new Class[]{BrightnessControlCell.class}, new String[]{"seekBarView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_player_progress));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{ThemeTypeCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{ThemeTypeCell.class}, new String[]{"checkImage"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_featuredStickers_addedIcon));
        themeDescriptions.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_PROGRESSBAR, new Class[]{TextSizeCell.class}, new String[]{"sizeBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_player_progress));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, new String[]{"sizeBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_player_progressBackground));
        themeDescriptions.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_PROGRESSBAR, new Class[]{BubbleRadiusCell.class}, new String[]{"sizeBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_player_progress));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{BubbleRadiusCell.class}, new String[]{"sizeBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_player_progressBackground));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{ChatListCell.class}, null, null, null, Theme.key_radioBackground));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{ChatListCell.class}, null, null, null, Theme.key_radioBackgroundChecked));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText2));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrack));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrackChecked));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgInDrawable, Theme.chat_msgInMediaDrawable}, null, Theme.key_chat_inBubble));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgInSelectedDrawable, Theme.chat_msgInMediaSelectedDrawable}, null, Theme.key_chat_inBubbleSelected));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, Theme.chat_msgInDrawable.getShadowDrawables(), null, Theme.key_chat_inBubbleShadow));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, Theme.chat_msgInMediaDrawable.getShadowDrawables(), null, Theme.key_chat_inBubbleShadow));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, null, Theme.key_chat_outBubble));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, null, Theme.key_chat_outBubbleGradient1));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, null, Theme.key_chat_outBubbleGradient2));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, null, Theme.key_chat_outBubbleGradient3));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutSelectedDrawable, Theme.chat_msgOutMediaSelectedDrawable}, null, Theme.key_chat_outBubbleSelected));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, null, Theme.key_chat_outBubbleShadow));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgInDrawable, Theme.chat_msgInMediaDrawable}, null, Theme.key_chat_inBubbleShadow));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_messageTextIn));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_messageTextOut));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckDrawable}, null, Theme.key_chat_outSentCheck));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckSelectedDrawable}, null, Theme.key_chat_outSentCheckSelected));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckReadDrawable, Theme.chat_msgOutHalfCheckDrawable}, null, Theme.key_chat_outSentCheckRead));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckReadSelectedDrawable, Theme.chat_msgOutHalfCheckSelectedDrawable}, null, Theme.key_chat_outSentCheckReadSelected));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgMediaCheckDrawable, Theme.chat_msgMediaHalfCheckDrawable}, null, Theme.key_chat_mediaSentCheck));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_inReplyLine));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_outReplyLine));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_inReplyNameText));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_outReplyNameText));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_inReplyMessageText));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_outReplyMessageText));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_inReplyMediaMessageSelectedText));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_outReplyMediaMessageSelectedText));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_inTimeText));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_outTimeText));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_inTimeSelectedText));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_outTimeSelectedText));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{AppIconsSelectorCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{AppIconsSelectorCell.class}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{AppIconsSelectorCell.class}, null, null, null, Theme.key_windowBackgroundWhiteHintText));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{AppIconsSelectorCell.class}, null, null, null, Theme.key_windowBackgroundWhiteValueText));
        themeDescriptions.addAll(SimpleThemeDescription.createThemeDescriptions(new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda13
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                m12309lambda$getThemeDescriptions$24$orgtelegramuiThemeActivity();
            }

        }, Theme.key_windowBackgroundWhiteHintText, Theme.key_windowBackgroundWhiteBlackText, Theme.key_windowBackgroundWhiteValueText));
        return themeDescriptions;
    }

    /* JADX INFO: renamed from: lambda$getThemeDescriptions$24$org-telegram-ui-ThemeActivity, reason: not valid java name */
    /* synthetic */ void m12309lambda$getThemeDescriptions$24$orgtelegramuiThemeActivity() {
        for (int i = 0; i < this.listView.getChildCount(); i++) {
            View ch = this.listView.getChildAt(i);
            if (ch instanceof AppIconsSelectorCell) {
                ((AppIconsSelectorCell) ch).getAdapter().notifyDataSetChanged();
            } else if (ch instanceof PeerColorActivity.ChangeNameColorCell) {
                ((PeerColorActivity.ChangeNameColorCell) ch).updateColors();
            }
        }
        for (int i2 = 0; i2 < this.listView.getCachedChildCount(); i2++) {
            View ch2 = this.listView.getCachedChildAt(i2);
            if (ch2 instanceof AppIconsSelectorCell) {
                ((AppIconsSelectorCell) ch2).getAdapter().notifyDataSetChanged();
            } else if (ch2 instanceof PeerColorActivity.ChangeNameColorCell) {
                ((PeerColorActivity.ChangeNameColorCell) ch2).updateColors();
            }
        }
        for (int i3 = 0; i3 < this.listView.getHiddenChildCount(); i3++) {
            View ch3 = this.listView.getHiddenChildAt(i3);
            if (ch3 instanceof AppIconsSelectorCell) {
                ((AppIconsSelectorCell) ch3).getAdapter().notifyDataSetChanged();
            } else if (ch3 instanceof PeerColorActivity.ChangeNameColorCell) {
                ((PeerColorActivity.ChangeNameColorCell) ch3).updateColors();
            }
        }
        for (int i4 = 0; i4 < this.listView.getAttachedScrapChildCount(); i4++) {
            View ch4 = this.listView.getAttachedScrapChildAt(i4);
            if (ch4 instanceof AppIconsSelectorCell) {
                ((AppIconsSelectorCell) ch4).getAdapter().notifyDataSetChanged();
            } else if (ch4 instanceof PeerColorActivity.ChangeNameColorCell) {
                ((PeerColorActivity.ChangeNameColorCell) ch4).updateColors();
            }
        }
    }

    public void checkCurrentDayNight() {
        if (this.currentType != 3) {
            return;
        }
        boolean toDark = !Theme.isCurrentThemeDay();
        if (this.lastIsDarkTheme != toDark) {
            this.lastIsDarkTheme = toDark;
            this.sunDrawable.setCustomEndFrame(toDark ? this.sunDrawable.getFramesCount() - 1 : 0);
            this.menuItem.getIconView().playAnimation();
        }
        if (this.themeListRow2 >= 0) {
            for (int i = 0; i < this.listView.getChildCount(); i++) {
                if (this.listView.getChildAt(i) instanceof DefaultThemesPreviewCell) {
                    DefaultThemesPreviewCell cell = (DefaultThemesPreviewCell) this.listView.getChildAt(i);
                    cell.updateDayNightMode();
                }
            }
        }
    }
}
