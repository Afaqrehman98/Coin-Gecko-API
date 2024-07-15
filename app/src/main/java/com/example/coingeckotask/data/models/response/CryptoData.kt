package com.example.coingeckotask.data.models.response

import com.google.gson.annotations.SerializedName

data class CryptoData(
    @SerializedName("bitcoin") val bitcoin: Bitcoin
)

data class Bitcoin(
    @SerializedName("usd") val usd: Double,
    @SerializedName("last_updated_at") val lastUpdatedAt: Long
)
