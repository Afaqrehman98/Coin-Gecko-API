package com.example.coingeckotask.data.models.response

import com.google.gson.annotations.SerializedName
import com.google.gson.*
import java.lang.reflect.Type

data class CryptoData(
    @SerializedName("bitcoin") val bitcoin: Bitcoin
)

data class Bitcoin(
    val currencies: Map<String, Long>,
    @SerializedName("last_updated_at") val lastUpdatedAt: Long
)


class BitcoinDeserializer : JsonDeserializer<Bitcoin> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Bitcoin {
        val jsonObject = json.asJsonObject
        val lastUpdatedAt = jsonObject.get("last_updated_at").asLong
        jsonObject.remove("last_updated_at")

        val currencies = mutableMapOf<String, Long>()
        for (entry in jsonObject.entrySet()) {
            currencies[entry.key] = entry.value.asLong
        }

        return Bitcoin(currencies, lastUpdatedAt)
    }
}