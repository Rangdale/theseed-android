package com.theseed.app.di

import com.theseed.app.data.remote.AnalyticsApi
import com.theseed.app.data.remote.AuthApi
import com.theseed.app.data.remote.DisciplineScoreApi
import com.theseed.app.data.remote.HabitApi
import com.theseed.app.data.remote.HomeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://theseed-backend.onrender.com/") // ← your real Render URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideHabitApi(retrofit: Retrofit): HabitApi =
        retrofit.create(HabitApi::class.java)

    @Provides
    @Singleton
    fun provideDisciplineScoreApi(retrofit: Retrofit): DisciplineScoreApi =
        retrofit.create(DisciplineScoreApi::class.java)

    @Provides
    @Singleton
    fun provideHomeApi(retrofit: Retrofit): HomeApi =
        retrofit.create(HomeApi::class.java)

    @Provides
    @Singleton
    fun provideAnalyticsApi(retrofit: Retrofit): AnalyticsApi =
        retrofit.create(AnalyticsApi::class.java)
}