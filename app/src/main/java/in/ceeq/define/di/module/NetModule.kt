package `in`.ceeq.define.di.module

import `in`.ceeq.define.BuildConfig
import `in`.ceeq.define.data.source.DefinitionApi
import `in`.ceeq.define.data.source.RandomWordApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class NetModule {

    @Module
    companion object {

        @JvmStatic
        @Singleton
        @Provides
        fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        @JvmStatic
        @Singleton
        @Provides
        fun provideOkHttp(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
                OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .addInterceptor(loggingInterceptor)
                        .build()

        @Singleton
        @JvmStatic
        @Provides
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
                Retrofit.Builder()
                        .baseUrl("https://glosbe.com/gapi/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(okHttpClient)
                        .build()

        @Singleton
        @JvmStatic
        @Provides
        @Named("random")
        fun provideRetrofitRandom(okHttpClient: OkHttpClient): Retrofit =
                Retrofit.Builder()
                        .baseUrl("https://api.wordnik.com/v4/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(okHttpClient)
                        .build()


        @JvmStatic
        @Singleton
        @Provides
        fun provideDefinitionApi(retrofit: Retrofit): DefinitionApi =
                retrofit.create(DefinitionApi::class.java)

        @JvmStatic
        @Singleton
        @Provides
        fun provideRandomWorldApi(@Named("random") retrofit: Retrofit): RandomWordApi =
                retrofit.create(RandomWordApi::class.java)

    }

}
