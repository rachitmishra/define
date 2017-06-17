package in.ceeq.define.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    private static final String PREFERENCE_FILE_NAME = "common_preferences";
    private final SharedPreferences mSharedPreference;

    private static final String SELECTED_LANGUAGE_POSITION = "selected_language_position";
    public static final String UPDATE_AVAILABLE = "update_available";
    public static final String UPDATE_VERSION = "update_version";
    public static final String UNIQUE_ID = "unique_id";

    public int getSelectedLanguagePosition() {
        return getIntegerPrefs(SELECTED_LANGUAGE_POSITION);
    }

    public void setSelectedLanguagePosition(int position) {
        set(SELECTED_LANGUAGE_POSITION, position);
    }

    public void setForceUpgradeStatus(boolean updateAvailable, int newVersion) {
        set(UPDATE_AVAILABLE, updateAvailable);
        set(UPDATE_VERSION, newVersion);
    }

    public boolean isUpdateAvailable() {
        return getBooleanPrefs(UPDATE_AVAILABLE);
    }

    public int getUpdateVersion() {
        return getIntegerPrefs(UPDATE_VERSION);
    }


    private PreferenceUtils(Context context) {
        mSharedPreference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    }

    public PreferenceUtils(Context context, String preferenceFileName) {
        mSharedPreference = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
    }

    public PreferenceUtils(Context context, int dummy) {
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceUtils newInstance(Context context) {
        return new PreferenceUtils(context);
    }

    public boolean getBooleanPrefs(String key) {
        return getBooleanPrefs(key, false);
    }

    public boolean getBooleanPrefs(String key, Boolean defaultValue) {
        return mSharedPreference.getBoolean(key, defaultValue);
    }

    public String getStringPrefs(String key) {
        return mSharedPreference.getString(key, "");
    }

    public String getStringPrefs(String key, String defaultValue) {
        return mSharedPreference.getString(key, defaultValue);
    }

    public int getIntegerPrefs(String key) {
        return mSharedPreference.getInt(key, 0);
    }

    public int getIntegerPrefs(String key, int defaultValue) {
        return mSharedPreference.getInt(key, defaultValue);
    }

    public long getLongPrefs(String key) {
        return mSharedPreference.getLong(key, 0L);
    }

    public long getLongPrefs(String key, long defaultValue) {
        return mSharedPreference.getLong(key, defaultValue);
    }


    public float getFloatPrefs(String key) {
        return mSharedPreference.getFloat(key, 0);
    }

    public void set(final String key, final Object value) {
        final SharedPreferences.Editor sharedPreferenceEditor = mSharedPreference.edit();
        if (value instanceof String) {
            sharedPreferenceEditor.putString(key, (String) value);
        } else if (value instanceof Long) {
            sharedPreferenceEditor.putLong(key, (Long) value);
        } else if (value instanceof Integer) {
            sharedPreferenceEditor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            sharedPreferenceEditor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            sharedPreferenceEditor.putFloat(key, (Float) value);
        }
        sharedPreferenceEditor.apply();
    }

    public void clear() {
        mSharedPreference.edit().clear().commit();
    }

    public boolean isKeyPresent(String key) {
        return mSharedPreference.contains(key);
    }

    public void setUniqueId(String uniqueId) {
        set(UNIQUE_ID, uniqueId);
    }

    public String getUniqueId() {
        return getStringPrefs(UNIQUE_ID);
    }
}
