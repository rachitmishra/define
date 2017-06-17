package in.ceeq.define.utils;

import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import in.ceeq.define.BuildConfig;

public final class LogUtils {
    private static final String LOG_PREFIX = BuildConfig.APPLICATION_ID;
    private static final int LOG_PREFIX_LENGTH = BuildConfig.APPLICATION_ID.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;


    private LogUtils() {
    }

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    public static void LOGD(final String tag, String message) {
        if (BuildConfig.DEBUG || Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message);
        }
    }

    public static void LOGD(final String tag, String message, Throwable cause) {
        if (BuildConfig.DEBUG || Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message, cause);
        }
    }

    public static void LOGW(final String tag, String message) {
        Log.w(tag, message);
    }

    public static void LOGW(final String tag, String message, Throwable cause) {
        Log.w(tag, message, cause);
    }

    public static void LOGE(final String tag, String message) {
        Log.e(tag, message);
    }

    public static void LOGE(final String tag, String message, Throwable cause) {
        Log.e(tag, message, cause);
    }

    public static void LOG(Exception e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        } else {
            FirebaseCrash.report(e);
        }
    }

    public static void LOG(String error) {
        if (BuildConfig.DEBUG) {
            LOGD(BuildConfig.APPLICATION_ID, error);
        } else {
            FirebaseCrash.log(error);
        }
    }

    public static void LOG(Error e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        } else {
            FirebaseCrash.log(e.getMessage());
        }
    }
}
