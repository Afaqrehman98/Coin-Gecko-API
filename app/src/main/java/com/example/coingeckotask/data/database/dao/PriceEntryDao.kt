package com.example.coingeckotask.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.coingeckotask.data.models.response.PriceEntry

@Dao
interface PriceEntryDao {

    @Query("SELECT * FROM price_entry")
    suspend fun getPriceHistory(): List<PriceEntry>

    @Upsert
    suspend fun insertPriceHistory(item: List<PriceEntry>)

    @Query("DELETE  FROM price_entry")
    suspend fun clearTable()
}