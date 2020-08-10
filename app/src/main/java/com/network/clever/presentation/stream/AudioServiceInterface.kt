package com.network.clever.presentation.stream

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.presentation.stream.AudioService.AudioServiceBinder


class AudioServiceInterface(context: Context) {
    private var mServiceConnection: ServiceConnection?
    private var mService: AudioService? = null
    fun setPlayList(audioIds: ArrayList<Long>) {
        mService?.setPlayList(audioIds)
    }

    fun play(position: Int) {
        mService?.play(position)
    }

    fun play() {
        mService?.play()
    }

    fun togglePlay() {
        if (isPlaying) {
            mService?.pause()
        } else {
            mService?.play()
        }
    }

    val isPlaying: Boolean
        get() = mService?.isPlaying ?: false

    val audioItem: MusicListModel.MusicModel?
        get() = mService?.audioItem

    fun pause() {
        mService?.play()
    }

    fun forward() {
        mService?.forward()
    }

    fun rewind() {
        mService?.rewind()
    }

    init {
        mServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(
                name: ComponentName,
                service: IBinder
            ) {
                mService = (service as AudioServiceBinder).service
            }

            override fun onServiceDisconnected(name: ComponentName) {
                mServiceConnection = null
                mService = null
            }
        }
        context.bindService(
            Intent(context, AudioService::class.java).setPackage(context.packageName),
            mServiceConnection!!,
            Context.BIND_AUTO_CREATE
        )
    }
}