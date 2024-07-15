package com.example.coingeckotask.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.coingeckotask.data.models.response.PriceEntry

@Dao
interface PriceEntryDao {

    @Query("SELECT * FROM price_entry")
    suspend fun getPriceHistory(): List<PriceEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceHistory(item: List<PriceEntry>)
}