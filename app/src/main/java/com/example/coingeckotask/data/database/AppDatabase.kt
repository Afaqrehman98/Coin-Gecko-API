package com.example.coingeckotask.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.coingeckotask.data.database.dao.PriceEntryDao
import com.example.coingeckotask.data.models.response.PriceEntry

@Database(entities = [PriceEntry::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun priceEntryDao(): PriceEntryDao
}