package `in`.ceeq.define.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtils private constructor() {

    private lateinit var mSharedPreference: SharedPreferences

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

    private constructor(context: Context) : this() {
        mSharedPreference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
    }

    private constructor(context: Context, preferenceFileName: String) : this() {
        mSharedPreference = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE)
    }

    private fun getBooleanPrefs(key: String, defaultValue: Boolean = false): Boolean {
        return mSharedPreference.getBoolean(key, defaultValue)
    }

    private fun getStringPrefs(key: String, defaultValue: String = ""): String {
        return mSharedPreference.getString(key, defaultValue)
    }

    private fun getIntegerPrefs(key: String, defaultValue: Int = 0): Int {
        return mSharedPreference.getInt(key, defaultValue)
    }

    fun getLongPrefs(key: String, defaultValue: Long = 0): Long {
        return mSharedPreference.getLong(key, defaultValue)
    }

    fun getFloatPrefs(key: String): Float {
        return mSharedPreference.getFloat(key, 0f)
    }

    private operator fun set(key: String, value: Any) {
        val sharedPreferenceEditor = mSharedPreference.edit()
        if (value is String) {
            sharedPreferenceEditor.putString(key, value)
        } else if (value is Long) {
            sharedPreferenceEditor.putLong(key, value)
        } else if (value is Int) {
            sharedPreferenceEditor.putInt(key, value)
        } else if (value is Boolean) {
            sharedPreferenceEditor.putBoolean(key, value)
        } else if (value is Float) {
            sharedPreferenceEditor.putFloat(key, value)
        }
        sharedPreferenceEditor.apply()
    }

    fun clear() {
        mSharedPreference.edit().clear().apply()
    }

    fun isKeyPresent(key: String): Boolean {
        return mSharedPreference.contains(key)
    }

    companion object {

        private val PREFERENCE_FILE_NAME = "common_preferences"

        private val SELECTED_LANGUAGE_POSITION = "selected_language_position"
        val UPDATE_AVAILABLE = "update_available"
        val UPDATE_VERSION = "update_version"
        val UNIQUE_ID = "unique_id"

        fun newInstance(context: Context): PreferenceUtils {
            return PreferenceUtils(context)
        }
    }
}
