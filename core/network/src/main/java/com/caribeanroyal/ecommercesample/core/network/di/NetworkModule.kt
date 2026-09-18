package com.caribeanroyal.ecommercesample.core.network.di

import com.caribeanroyal.ecommercesample.core.network.api.CruiseApiService
import com.caribeanroyal.ecommercesample.core.network.interceptor.MockCruiseInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(MockCruiseInterceptor())
            .build()
    }

    // Simulating a backend URL
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.royalcaribbean.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideCruiseApiService(retrofit: Retrofit): CruiseApiService {
        return retrofit.create(CruiseApiService::class.java)
    }
}
