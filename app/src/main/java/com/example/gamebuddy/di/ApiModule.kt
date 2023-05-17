package com.example.gamebuddy.di

import com.example.gamebuddy.BuildConfig
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import com.example.gamebuddy.data.remote.network.GameBuddyApiCommunityService
import com.example.gamebuddy.data.remote.network.GameBuddyApiMatchService
import com.example.gamebuddy.data.remote.network.GameBuddyApiMessageService
import com.example.gamebuddy.util.BaseUrlInterceptor
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideBaseUrlInterceptor(): BaseUrlInterceptor {
        return BaseUrlInterceptor()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        baseUrlInterceptor: BaseUrlInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(baseUrlInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(500L, TimeUnit.SECONDS)
            .writeTimeout(300L, TimeUnit.SECONDS)
            .readTimeout(300L, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideGameBuddyAuthService(
        retrofit: Retrofit
    ): GameBuddyApiAuthService {
        return retrofit.create(GameBuddyApiAuthService::class.java)
    }

    @Singleton
    @Provides
    fun provideGameBuddyAppService(
        retrofit: Retrofit
    ): GameBuddyApiAppService {
        return retrofit.create(GameBuddyApiAppService::class.java)
    }

    @Singleton
    @Provides
    fun provideGameBuddyMatchService(
        retrofit: Retrofit
    ): GameBuddyApiMatchService {
        return retrofit.create(GameBuddyApiMatchService::class.java)
    }

    @Singleton
    @Provides
    fun provideGameBuddyCommunityService(
        retrofit: Retrofit
    ): GameBuddyApiCommunityService {
        return retrofit.create(GameBuddyApiCommunityService::class.java)
    }

    @Singleton
    @Provides
    fun provideGameBuddyMessageService(
        retrofit: Retrofit
    ): GameBuddyApiMessageService {
        return retrofit.create(GameBuddyApiMessageService::class.java)
    }

}