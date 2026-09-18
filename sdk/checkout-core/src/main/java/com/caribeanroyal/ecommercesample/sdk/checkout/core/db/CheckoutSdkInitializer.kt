package com.caribeanroyal.ecommercesample.sdk.checkout.core.db

import android.content.Context
import androidx.annotation.Keep
import androidx.room.Room
import androidx.startup.Initializer

@Keep
class CheckoutSdkInitializer : Initializer<CheckoutDatabase> {
    override fun create(context: Context): CheckoutDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CheckoutDatabase::class.java,
            "checkout_sdk_db"
        ).build()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
