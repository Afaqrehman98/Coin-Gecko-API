package com.example.coingeckotask.data.repositories

import com.example.coingeckotask.data.models.response.SupportedCurrency
import com.example.coingeckotask.data.network.ApiInterface
import com.example.coingeckotask.data.network.SafeApiRequest
import javax.inject.Inject

class CoinRepository@Inject constructor(
    private val api: ApiInterface
) : SafeApiRequest() {
    suspend fun callSupportedCurrency(
    ): SupportedCurrency = apiRequest {
        api.callSupportedCurrency()
    }
}