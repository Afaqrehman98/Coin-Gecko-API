package com.example.coingeckotask.data.models.response

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement

import java.lang.reflect.Type

data class PriceEntry(
    val timestamp: Long,
    val price: Double
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
        return PriceEntry(timestamp, price)
    }
}