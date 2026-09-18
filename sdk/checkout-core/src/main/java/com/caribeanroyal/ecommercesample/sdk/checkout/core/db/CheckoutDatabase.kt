package com.caribeanroyal.ecommercesample.sdk.checkout.core.db

import androidx.annotation.Keep
import androidx.room.Database
import androidx.room.RoomDatabase

@Keep
@Database(entities = [IdempotencyEntity::class], version = 1, exportSchema = false)
abstract class CheckoutDatabase : RoomDatabase() {
    abstract fun idempotencyDao(): IdempotencyDao
}
