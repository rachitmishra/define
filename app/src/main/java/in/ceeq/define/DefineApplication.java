package in.ceeq.define;

import android.app.Application;

import in.ceeq.define.utils.AnalyticsUtils;

public class DefineApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new AnalyticsUtils().init(this);
    }
}
