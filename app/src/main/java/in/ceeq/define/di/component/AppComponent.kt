package `in`.ceeq.define.di.component

import `in`.ceeq.define.DefineApplication
import `in`.ceeq.define.di.module.ActivityBindings
import `in`.ceeq.define.di.module.AppModule
import `in`.ceeq.define.di.module.DataModule
import `in`.ceeq.define.di.module.NetModule
import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, NetModule::class, ActivityBindings::class, DataModule::class, AndroidInjectionModule::class])
interface AppComponent : AndroidInjector<DefineApplication> {
    fun inject(application: Application)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun app(application: Application): Builder

        fun build(): AppComponent
    }
}
