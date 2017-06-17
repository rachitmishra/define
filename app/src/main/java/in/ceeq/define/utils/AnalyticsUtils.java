package in.ceeq.define.utils;

import android.app.Application;


import in.ceeq.define.BuildConfig;

public class AnalyticsUtils {

    public void init(Application application) {
        if (!BuildConfig.DEBUG) {

        } else {
            // Uncomment if you want to use Stetho
            //Stetho.initializeWithDefaults(application);
        }
    }
}
