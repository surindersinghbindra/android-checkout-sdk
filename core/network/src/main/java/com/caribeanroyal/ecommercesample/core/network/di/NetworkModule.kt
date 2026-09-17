package com.caribeanroyal.ecommercesample.core.network.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    // Simulating a backend URL
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://mock.ecommerce.api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
