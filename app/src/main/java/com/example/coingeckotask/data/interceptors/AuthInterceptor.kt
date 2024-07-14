package com.example.coingeckotask.data.interceptors

import android.content.Context
import android.util.Log
import com.example.coingeckotask.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val apiKey = BuildConfig.API_KEY
        if (apiKey.isNotBlank()) {
            Log.e("AuthInterceptor","The API key is ${apiKey}")
            requestBuilder.addHeader("accept", "application/json")
            requestBuilder.addHeader("x-cg-demo-api-key", apiKey)
        }
        return chain.proceed(requestBuilder.build())
    }
}
