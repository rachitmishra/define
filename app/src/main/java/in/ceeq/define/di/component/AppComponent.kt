package `in`.ceeq.define.di.component

import `in`.ceeq.define.DefineApplication
import `in`.ceeq.define.di.module.ActivityBindingModule
import `in`.ceeq.define.di.module.AppModule
import `in`.ceeq.define.di.module.DataModule
import `in`.ceeq.define.di.module.NetModule
import android.app.Application
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class,
        DataModule::class,
        NetModule::class,
        ActivityBindingModule::class))
interface AppComponent : AndroidInjector<DefineApplication> {
    fun inject(application: Application)
}
