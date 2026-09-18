package com.caribeanroyal.ecommercesample.sdk.checkout.core.db

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "idempotency_keys")
data class IdempotencyEntity(
    @PrimaryKey val idempotencyKey: String,
    val timestamp: Long = System.currentTimeMillis()
)
