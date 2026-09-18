package com.caribeanroyal.ecommercesample.core.network.di

import com.caribeanroyal.ecommercesample.core.domain.repository.CruiseRepository
import com.caribeanroyal.ecommercesample.core.network.repository.NetworkCruiseRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindCruiseRepository(
        impl: NetworkCruiseRepositoryImpl
    ): CruiseRepository
}
