package com.network.clever.utility.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.utility.player.AudioService.AudioServiceBinder
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants


class AudioServiceInterface(context: Context) {
    private var mServiceConnection: ServiceConnection
    lateinit var mService: AudioService

    init {
        mServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(
                name: ComponentName,
                service: IBinder
            ) {
                mService = (service as AudioServiceBinder).service
                initPlayer()
            }

            override fun onServiceDisconnected(name: ComponentName) {
            }
        }
        context.bindService(
            Intent(context, AudioService::class.java).setPackage(context.packageName),
            mServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    fun initPlayer() {
        mService.initPlayer()
    }

    fun setPlayList(mMusics: ArrayList<MusicListModel.MusicModel>, videoId: String) {
        mService.setPlayList(mMusics, videoId)
    }

    fun play(position: Int) {
        mService.play(position)
    }

    fun play() {
        mService.play()
    }

    fun pause() {
        mService.pause()
    }

    fun forward() {
        mService.forward()
    }

    fun rewind() {
        mService.rewind()
    }

    fun togglePlay() {
        when (playerState) {
            PlayerConstants.PlayerState.PLAYING -> pause()
            PlayerConstants.PlayerState.PAUSED -> play()
            else -> {
            }
        }
    }

    val playerState: PlayerConstants.PlayerState
        get() = mService.playerState

    val audioItem: MusicListModel.MusicModel?
        get() = mService.audioItem
}