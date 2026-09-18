package com.caribeanroyal.ecommercesample.di

import android.content.Context
import com.caribeanroyal.ecommercesample.core.database.di.DatabaseModule
import com.caribeanroyal.ecommercesample.core.network.di.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

import com.caribeanroyal.ecommercesample.core.domain.usecase.GetCruiseItineraryUseCase
import com.caribeanroyal.ecommercesample.core.network.di.RepositoryModule

@Singleton
@Component(modules = [NetworkModule::class, DatabaseModule::class, RepositoryModule::class])
interface AppComponent {

    fun getCruiseItineraryUseCase(): GetCruiseItineraryUseCase

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
