package com.caribeanroyal.ecommercesample.di

import android.content.Context
import com.caribeanroyal.ecommercesample.core.database.di.DatabaseModule
import com.caribeanroyal.ecommercesample.core.network.di.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, DatabaseModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
