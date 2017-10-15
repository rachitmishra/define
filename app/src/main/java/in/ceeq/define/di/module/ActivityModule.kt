package `in`.ceeq.define.di.module

import `in`.ceeq.define.data.DefineRepository
import `in`.ceeq.define.di.scope.ActivityScope
import `in`.ceeq.define.ui.viewmodel.DefinitionViewModel
import `in`.ceeq.define.utils.PreferenceUtils
import android.app.Application
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
abstract class ActivityModule {

    @Module
    companion object {

        @JvmStatic
        @ActivityScope
        @Provides
        fun provideDefineRepository(retrofit: Retrofit): DefineRepository
                = DefineRepository.create(retrofit)

        @JvmStatic
        @ActivityScope
        @Provides
        fun provideResources(app: Application): Resources = app.resources

        @JvmStatic
        @ActivityScope
        @Provides
        fun provideDefinitionViewModel(resources: Resources,
                                       preferenceUtils: PreferenceUtils,
                                       defineRepository: DefineRepository): DefinitionViewModel =
                DefinitionViewModel(resources, preferenceUtils, defineRepository)
    }

}
