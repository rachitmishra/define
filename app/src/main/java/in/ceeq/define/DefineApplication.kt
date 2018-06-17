package `in`.ceeq.define

import `in`.ceeq.define.di.component.DaggerAppComponent
import `in`.ceeq.define.utils.AnalyticsUtils
import android.app.Activity
import android.app.Application
import android.app.Service
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject


class DefineApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        AnalyticsUtils().init(this)

        DaggerAppComponent.builder()
                .app(this)
                .build().inject(this)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> = androidInjector
}
