package `in`.ceeq.define.di.module

import `in`.ceeq.define.data.source.DefinitionDataSource
import `in`.ceeq.define.data.source.LocalDefinitionDataSource
import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DataModule {

    @Provides
    @Singleton
    fun providesLocalDataSource(application: Application) : DefinitionDataSource =
            LocalDefinitionDataSource.getInstance(application)
}
