package `in`.ceeq.define.ui.viewmodel


import `in`.ceeq.define.BR
import `in`.ceeq.define.BuildConfig
import `in`.ceeq.define.R
import `in`.ceeq.define.utils.PreferenceUtils
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Resources
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.net.Uri
import android.os.Build
import android.view.View
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class SettingsViewModel(val resources: Resources,
                        private val preferenceUtils: PreferenceUtils) : BaseObservable() {

    @get:Bindable
    var selectedLanguagePosition: Int = 0
        set(selectedLanguagePosition) {
            field = selectedLanguagePosition
            notifyPropertyChanged(BR.selectedLanguagePosition)
        }

    @get:Bindable
    var appVersion: String = ""
        set(appVersion) {
            field = appVersion
            notifyPropertyChanged(BR.appVersion)
        }

    @get:Bindable
    var updateAvailable: Boolean = false
        set(updateAvailable) {
            field = updateAvailable
            notifyPropertyChanged(BR.updateAvailable)
        }

    init {
        selectedLanguagePosition = preferenceUtils.selectedLanguagePosition
        appVersion = String.format(resources.getString(R.string.app_version_message), BuildConfig.VERSION_NAME)
        updateAvailable = FirebaseRemoteConfig.getInstance().getLong(FB_RC_CURRENT_VERSION) > BuildConfig.VERSION_CODE
    }

    fun close(view: View) {
        (view.context as Activity).finish()
    }

    fun rateApp(view: View) {
        val appPackageName = view.context.packageName
        try {
            view.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_APP_MARKET_URI + appPackageName)))
        } catch (e: ActivityNotFoundException) {
            view.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_APP_DETAILS_URI + appPackageName)))
        }

    }

    fun shareApp(view: View) {
        val shareString = view.context.getString(R.string.share_message)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareString)
        shareIntent.type = "text/plain"
        view.context.startActivity(Intent.createChooser(shareIntent,
                view.context.getString(R.string.share_via)))
    }

    fun feedbackApp(view: View) {
        var debugInfo = BuildConfig.VERSION_NAME
        debugInfo += "_" + BuildConfig.VERSION_CODE
        debugInfo += "_" + Build.VERSION.SDK_INT
        debugInfo += "_" + Build.VERSION.RELEASE
        debugInfo += "_" + Build.MANUFACTURER
        debugInfo += "_" + Build.MODEL

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_EMAIL, "support@ceeq.in")
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback - Define")
        shareIntent.putExtra(Intent.EXTRA_TEXT, debugInfo)
        shareIntent.type = "text/plain"
        view.context.startActivity(Intent.createChooser(shareIntent,
                view.context.getString(R.string.send_email)))

    }

    fun onLanguageSelected(view: View, position: Int) {
        PreferenceUtils.newInstance(view.context).selectedLanguagePosition = position
    }

    companion object {
        private val PLAY_STORE_APP_DETAILS_URI = "https://play.google.com/store/apps/details?id="
        private val PLAY_STORE_APP_MARKET_URI = "market://details?id="
        private val FB_RC_CURRENT_VERSION = "current_version"
    }
}

