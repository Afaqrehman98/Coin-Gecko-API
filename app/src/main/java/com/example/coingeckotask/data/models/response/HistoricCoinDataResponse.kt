package com.example.coingeckotask.data.models.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

@Entity(tableName = "price_entry")
data class PriceEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo val timestamp: Long,
    @ColumnInfo val price: Double
)

data class HistoricCoinDataResponse(
    val prices: List<PriceEntry>
)

class PriceEntryDeserializer : JsonDeserializer<PriceEntry> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): PriceEntry {
        val jsonArray = json?.asJsonArray
        val timestamp = jsonArray?.get(0)?.asLong ?: 0L
        val price = jsonArray?.get(1)?.asDouble ?: 0.0
        return PriceEntry(timestamp = timestamp, price = price)
    }
}