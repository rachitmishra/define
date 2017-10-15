package `in`.ceeq.define.utils


import `in`.ceeq.define.BuildConfig
import android.app.Application

class AnalyticsUtils {

    fun init(application: Application) {
        if (!BuildConfig.DEBUG) {

        } else {
            // Uncomment if you want to use Stetho
            //Stetho.initializeWithDefaults(application);
        }
    }
}
