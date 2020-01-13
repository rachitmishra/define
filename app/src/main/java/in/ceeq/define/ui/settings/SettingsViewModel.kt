package `in`.ceeq.define.ui.settings


import `in`.ceeq.define.BR
import `in`.ceeq.define.BuildConfig
import `in`.ceeq.define.R
import `in`.ceeq.define.utils.PreferenceUtils
import android.content.res.Resources
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import javax.inject.Inject

class SettingsViewModel @Inject constructor(resources: Resources,
                                            private val preferenceUtils: PreferenceUtils) :
        BaseObservable() {

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

    fun saveSelectedLanguagePosition(position: Int) {
        preferenceUtils.selectedLanguagePosition = position
    }

    companion object {
        private val FB_RC_CURRENT_VERSION = "current_version"
    }
}

