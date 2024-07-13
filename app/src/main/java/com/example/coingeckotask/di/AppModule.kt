package com.example.coingeckotask.di

import android.content.Context
import com.example.coingeckotask.data.interceptors.AuthInterceptor
import com.example.coingeckotask.data.interceptors.NetworkConnectionInterceptor
import com.example.coingeckotask.data.network.ApiInterface
import com.example.coingeckotask.utils.Constants.END_POINT
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideAuthRetrofitService(
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        authInterceptor: AuthInterceptor,
    ): ApiInterface {
        val WS_SERVER_URL = END_POINT
        val okkHttpclient = OkHttpClient.Builder()
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(authInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okkHttpclient)
            .baseUrl(WS_SERVER_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .build()
            .create(ApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideNetworkConnectionInterceptor(@ApplicationContext context: Context): NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(context)
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(@ApplicationContext context: Context): AuthInterceptor {
        return AuthInterceptor(context)
    }

}
