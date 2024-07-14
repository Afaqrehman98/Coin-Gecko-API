package com.example.coingeckotask.data.network

import com.example.coingeckotask.data.models.response.HistoricCoinDataResponse
import com.example.coingeckotask.data.models.response.SupportedCurrency
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiInterface {

    @GET("simple/supported_vs_currencies")
    suspend fun callSupportedCurrency(): Response<SupportedCurrency>

    @GET("coins/{id}/market_chart/range")
    suspend fun callCoinHistoricData(
        @Path("id") id: String?,
        @Query("vs_currency") vsCurrency: String?,
        @Query("from") from: Long?,
        @Query("to") to: Long?,
        @Query("precision") precision: String = "2"
    ): Response<HistoricCoinDataResponse>


}