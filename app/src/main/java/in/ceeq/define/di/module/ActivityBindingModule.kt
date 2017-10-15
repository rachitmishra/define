package `in`.ceeq.define.di.module

import `in`.ceeq.define.di.scope.ActivityScope
import `in`.ceeq.define.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(ActivityModule::class))
    abstract fun contributeMainActivity(): MainActivity

}
