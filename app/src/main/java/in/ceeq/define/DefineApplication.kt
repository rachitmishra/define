package `in`.ceeq.define

import `in`.ceeq.define.di.component.DaggerAppComponent
import `in`.ceeq.define.utils.AnalyticsUtils
import android.app.Activity
import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class DefineApplication : Application(), HasActivityInjector {

    override fun activityInjector(): DispatchingAndroidInjector<Activity> = mAndroidInjector

    @Inject
    lateinit var mAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        AnalyticsUtils().init(this)

        DaggerAppComponent.builder()
                .app(this)
                .build().inject(this)
    }


}
