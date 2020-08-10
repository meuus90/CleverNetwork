package com.network.clever

import android.app.Application
import com.network.clever.presentation.stream.AudioServiceInterface


class AudioApplication : Application() {
    lateinit var serviceInterface: AudioServiceInterface

    override fun onCreate() {
        super.onCreate()
        instance = this
        serviceInterface = AudioServiceInterface(applicationContext)
    }

    companion object {
        lateinit var instance: AudioApplication
    }
}
