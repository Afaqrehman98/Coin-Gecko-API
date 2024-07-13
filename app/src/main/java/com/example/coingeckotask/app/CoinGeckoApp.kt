package com.example.coingeckotask.app

import android.app.Application
import android.content.Context
import com.intuit.sdp.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CoinGeckoApp : Application() {

//    companion object {
//        var instance: CoinGeckoApp? = null
//            private set
//        var appContext: Context? = null
//            private set
//    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
//        instance = this
//        appContext = applicationContext
////        SharedPref.setStringPref(
//            this,
//            SharedPref.KEY_TOKEN_LENGTH,
//            getString(R.string.token_length)
//        )
    }
}