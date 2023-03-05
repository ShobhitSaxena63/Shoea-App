package com.example.shoea

import android.app.Application
import timber.log.Timber

class ShoeaApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}