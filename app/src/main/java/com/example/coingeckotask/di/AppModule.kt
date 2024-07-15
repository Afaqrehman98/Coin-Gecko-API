package com.example.coingeckotask.di

import android.content.Context
import androidx.room.Room
import com.example.coingeckotask.data.database.AppDatabase
import com.example.coingeckotask.data.database.dao.PriceEntryDao
import com.example.coingeckotask.data.interceptors.AuthInterceptor
import com.example.coingeckotask.data.interceptors.NetworkConnectionInterceptor
import com.example.coingeckotask.data.models.response.PriceEntry
import com.example.coingeckotask.data.models.response.PriceEntryDeserializer
import com.example.coingeckotask.data.network.ApiInterface
import com.example.coingeckotask.utils.Constants.BASE_URL
import com.google.gson.Gson
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
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(PriceEntry::class.java, PriceEntryDeserializer())
            .serializeNulls()
            .create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(authInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
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

    @InstallIn(SingletonComponent::class)
    @Module
    class DatabaseModule {

        @Provides
        fun provideVideoChannelDao(appDatabase: AppDatabase): PriceEntryDao {
            return appDatabase.priceEntryDao()
        }


        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
            return Room.databaseBuilder(
                appContext,
                AppDatabase::class.java,
                "AppDatabase"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }


}
