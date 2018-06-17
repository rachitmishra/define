package `in`.ceeq.define.utils

import `in`.ceeq.define.BuildConfig
import android.util.Log
import com.crashlytics.android.Crashlytics

object LogUtils {
    private val LOG_PREFIX = BuildConfig.APPLICATION_ID
    private val LOG_PREFIX_LENGTH = BuildConfig.APPLICATION_ID.length
    private val MAX_LOG_TAG_LENGTH = 23

    fun makeLogTag(str: String): String {
        return if (str.length > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1)
        } else LOG_PREFIX + str

    }

    fun log(message: String, tag: String = BuildConfig.APPLICATION_ID) {
        if (BuildConfig.DEBUG || Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message)
        }
    }

    fun log(message: String, cause: Throwable, tag: String = BuildConfig.APPLICATION_ID) {
        if (BuildConfig.DEBUG || Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message, cause)
        }
    }

    fun log(e: Exception) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace()
        } else {
            Crashlytics.logException(e)
        }
    }
}
