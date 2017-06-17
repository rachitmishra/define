package in.ceeq.define.databinding;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import in.ceeq.define.BR;
import in.ceeq.define.BuildConfig;
import in.ceeq.define.R;
import in.ceeq.define.utils.PreferenceUtils;

public class SettingsViewModel extends BaseObservable implements Parcelable {

    private static final String PLAY_STORE_APP_DETAILS_URI = "https://play.google.com/store/apps/details?id=";
    private static final String PLAY_STORE_APP_MARKET_URI = "market://details?id=";
    private static final String FB_RC_CURRENT_VERSION = "current_version";
    private int selectedLanguagePosition;
    private String appVersion;
    private boolean isUpdateAvailable;

    @Bindable
    public boolean getIsUpdateAvailable() {
        return isUpdateAvailable;
    }

    public void setUpdateAvailable(boolean updateAvailable) {
        isUpdateAvailable = updateAvailable;
        notifyPropertyChanged(BR.isUpdateAvailable);
    }

    @Bindable
    public String getAppVersion() {
        return appVersion;
    }

    private void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @Bindable
    public int getSelectedLanguagePosition() {
        return selectedLanguagePosition;
    }

    public void setSelectedLanguagePosition(int selectedLanguagePosition) {
        this.selectedLanguagePosition = selectedLanguagePosition;
        notifyPropertyChanged(BR.selectedLanguagePosition);
    }

    public SettingsViewModel(Context context) {
        setDefaults(context);
    }

    private void setDefaults(Context context) {
        selectedLanguagePosition = PreferenceUtils.newInstance(context).getSelectedLanguagePosition();
        setAppVersion(String.format(context.getString(R.string.app_version_message), BuildConfig.VERSION_NAME));
        setUpdateAvailable(FirebaseRemoteConfig.getInstance().getLong(FB_RC_CURRENT_VERSION)
                > BuildConfig.VERSION_CODE);
    }

    public void init(Context context) {
        setDefaults(context);
    }

    public void close(View view) {
        ((Activity) view.getContext()).finish();
    }

    public void rateApp(View view) {
        final String appPackageName = view.getContext().getPackageName();
        try {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_APP_MARKET_URI
                    + appPackageName)));
        } catch (ActivityNotFoundException e) {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_APP_DETAILS_URI
                    + appPackageName)));
        }
    }

    public void shareApp(View view) {
        String shareString = view.getContext().getString(R.string.share_message);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);
        shareIntent.setType("text/plain");
        view.getContext().startActivity(Intent.createChooser(shareIntent,
                view.getContext().getString(R.string.share_via)));
    }

    public void feedbackApp(View view) {
        String debugInfo = BuildConfig.VERSION_NAME;
        debugInfo += "_" + BuildConfig.VERSION_CODE;
        debugInfo += "_" + Build.VERSION.SDK_INT;
        debugInfo += "_" + Build.VERSION.RELEASE;
        debugInfo += "_" + Build.MANUFACTURER;
        debugInfo += "_" + Build.MODEL;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_EMAIL, "support@ceeq.in");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback - Define");
        shareIntent.putExtra(Intent.EXTRA_TEXT, debugInfo);
        shareIntent.setType("text/plain");
        view.getContext().startActivity(Intent.createChooser(shareIntent,
                view.getContext().getString(R.string.send_email)));

    }

    public void onLanguageSelected(View view, int position) {
        PreferenceUtils.newInstance(view.getContext()).setSelectedLanguagePosition(position);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.selectedLanguagePosition);
        dest.writeString(this.appVersion);
    }

    protected SettingsViewModel(Parcel in) {
        this.selectedLanguagePosition = in.readInt();
        this.appVersion = in.readString();
    }

    public static final Creator<SettingsViewModel> CREATOR = new Creator<SettingsViewModel>() {
        @Override
        public SettingsViewModel createFromParcel(Parcel source) {
            return new SettingsViewModel(source);
        }

        @Override
        public SettingsViewModel[] newArray(int size) {
            return new SettingsViewModel[size];
        }
    };
}

