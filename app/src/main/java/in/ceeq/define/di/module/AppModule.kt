package `in`.ceeq.define.di.module

import `in`.ceeq.define.utils.PreferenceUtils
import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun providesApplication(): Application = application

    @Provides
    @Singleton
    fun providesPreferenceUtils(): PreferenceUtils = PreferenceUtils.newInstance(application)
}
