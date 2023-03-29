package com.pessoadev.marvelapp

import android.app.Application
import com.pessoadev.marvelapp.di.module.AppModule
import com.pessoadev.marvelapp.di.module.NetworkModule
import com.pessoadev.marvelapp.di.module.dbModule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class MarvelKoinTest : KoinTest {

    @MockK
    private lateinit var context: Application

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun checkModules() {
        startKoin {
            androidContext(context)
            modules(listOf(AppModule, NetworkModule,dbModule))
        }.checkModules()
    }
}