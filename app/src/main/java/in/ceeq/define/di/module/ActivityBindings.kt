package `in`.ceeq.define.di.module

import `in`.ceeq.define.di.scope.ActivityScope
import `in`.ceeq.define.ui.home.HomeActivity
import `in`.ceeq.define.ui.settings.SettingsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindings {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeSettingsActivity(): SettingsActivity

}
