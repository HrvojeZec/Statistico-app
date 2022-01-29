package com.example.statistico

import android.app.Application
import android.content.Context

class StatisticoApplication : Application() {
    companion object {
        lateinit var ApplicationContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        ApplicationContext = this
    }
}