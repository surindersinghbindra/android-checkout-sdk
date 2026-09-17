package com.caribeanroyal.ecommercesample.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.caribeanroyal.ecommercesample.core.database.AppDatabase

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ecommerce-db"
        ).build()
    }
}
