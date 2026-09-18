package com.caribeanroyal.ecommercesample.sdk.checkout.core.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IdempotencyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertKey(entity: IdempotencyEntity)

    @Query("SELECT COUNT(*) FROM idempotency_keys WHERE idempotencyKey = :key")
    suspend fun exists(key: String): Int
}
