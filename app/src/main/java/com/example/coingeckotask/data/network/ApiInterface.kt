package com.example.coingeckotask.data.network

import com.example.coingeckotask.data.models.response.SupportedCurrency
import retrofit2.Response
import retrofit2.http.GET


interface ApiInterface {

    @GET("supported_vs_currencies")
    suspend fun callSupportedCurrency(): Response<SupportedCurrency>


}