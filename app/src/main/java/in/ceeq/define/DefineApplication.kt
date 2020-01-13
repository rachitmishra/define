package `in`.ceeq.define

import `in`.ceeq.define.di.component.DaggerAppComponent
import `in`.ceeq.define.utils.AnalyticsUtils
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class DefineApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        AnalyticsUtils().init(this)

        DaggerAppComponent.builder()
                .app(this)
                .build().inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}
