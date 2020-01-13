package `in`.ceeq.define.utils

import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceUtils @Inject constructor(val mSharedPreferences: SharedPreferences) {

    var selectedLanguagePosition: Int
        get() = getIntegerPrefs(SELECTED_LANGUAGE_POSITION)
        set(position) = set(SELECTED_LANGUAGE_POSITION, position)

    val isUpdateAvailable: Boolean
        get() = getBooleanPrefs(UPDATE_AVAILABLE)

    val updateVersion: Int
        get() = getIntegerPrefs(UPDATE_VERSION)

    var uniqueId: String
        get() = getStringPrefs(UNIQUE_ID)
        set(uniqueId) = set(UNIQUE_ID, uniqueId)

    fun setForceUpgradeStatus(updateAvailable: Boolean, newVersion: Int) {
        set(UPDATE_AVAILABLE, updateAvailable)
        set(UPDATE_VERSION, newVersion)
    }

    private operator fun set(key: String, value: Any) {
        val sharedPreferenceEditor = mSharedPreferences.edit()
        when (value) {
            is String -> sharedPreferenceEditor.putString(key, value)
            is Long -> sharedPreferenceEditor.putLong(key, value)
            is Int -> sharedPreferenceEditor.putInt(key, value)
            is Boolean -> sharedPreferenceEditor.putBoolean(key, value)
            is Float -> sharedPreferenceEditor.putFloat(key, value)
        }
        sharedPreferenceEditor.apply()
    }

    fun clear() {
        mSharedPreferences.edit().clear().apply()
    }

    fun isKeyPresent(key: String): Boolean {
        return mSharedPreferences.contains(key)
    }

    companion object {
        private val SELECTED_LANGUAGE_POSITION = "selected_language_position"
        val UPDATE_AVAILABLE = "update_available"
        val UPDATE_VERSION = "update_version"
        val UNIQUE_ID = "unique_id"
    }
}

fun PreferenceUtils.getFloat(key: String, defaultValue: Float = 0F): Float {
    return mSharedPreferences.getFloat(key, defaultValue)
}

fun PreferenceUtils.getBooleanPrefs(key: String, defaultValue: Boolean = false): Boolean {
    return mSharedPreferences.getBoolean(key, defaultValue)
}

fun PreferenceUtils.getStringPrefs(key: String, defaultValue: String = ""): String {
    return mSharedPreferences.getString(key, defaultValue) ?: ""
}

fun PreferenceUtils.getIntegerPrefs(key: String, defaultValue: Int = 0): Int {
    return mSharedPreferences.getInt(key, defaultValue)
}

fun PreferenceUtils.getLongPrefs(key: String, defaultValue: Long = 0L): Long {
    return mSharedPreferences.getLong(key, defaultValue)
}

