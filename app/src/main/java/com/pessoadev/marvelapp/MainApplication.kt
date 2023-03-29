package com.pessoadev.marvelapp

import android.app.Application
import com.pessoadev.marvelapp.di.module.AppModule
import com.pessoadev.marvelapp.di.module.NetworkModule
import com.pessoadev.marvelapp.di.module.dbModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(listOf(AppModule, NetworkModule,dbModule))
        }

    }
}