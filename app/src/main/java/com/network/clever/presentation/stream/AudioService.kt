package com.network.clever.presentation.stream

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.network.clever.data.datasource.model.item.MusicListModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener


class AudioService : Service() {
    private val mBinder: IBinder = AudioServiceBinder()
    private val mMusics: ArrayList<MusicListModel.MusicModel> = ArrayList()
    private var mMediaPlayer: YouTubePlayer? = null
    private var isPrepared = false
    private var mCurrentPosition = 0
    private var mNotificationPlayer: NotificationPlayer? = null

    var isPlaying = false
    val audioItem: MusicListModel.MusicModel?
        get() = try {
            mMusics[mCurrentPosition]
        } catch (e: Exception) {
            null
        }

    inner class AudioServiceBinder : Binder() {
        val service: AudioService
            get() = this@AudioService
    }

    fun setPlayer(mediaPlayer: YouTubePlayer) {
        mMediaPlayer = mediaPlayer
        mMediaPlayer?.addListener(object : YouTubePlayerListener {
            override fun onApiChange(youTubePlayer: YouTubePlayer) {

            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {

            }

            override fun onError(
                youTubePlayer: YouTubePlayer,
                error: PlayerConstants.PlayerError
            ) {
                isPrepared = false
                sendBroadcast(Intent(BroadcastActions.PLAY_STATE_CHANGED)) // 재생상태 변경 전송
                updateNotificationPlayer()
            }

            override fun onPlaybackQualityChange(
                youTubePlayer: YouTubePlayer,
                playbackQuality: PlayerConstants.PlaybackQuality
            ) {

            }

            override fun onPlaybackRateChange(
                youTubePlayer: YouTubePlayer,
                playbackRate: PlayerConstants.PlaybackRate
            ) {

            }

            override fun onReady(youTubePlayer: YouTubePlayer) {
                isPrepared = true
                youTubePlayer.play()
                sendBroadcast(Intent(BroadcastActions.PREPARED)) // prepared 전송
                updateNotificationPlayer()
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                when (state) {
                    PlayerConstants.PlayerState.ENDED -> {
                        if (mCurrentPosition < mMusics.lastIndex) {
                            forward()
                        } else {
                            isPrepared = false
                            sendBroadcast(Intent(BroadcastActions.PLAY_STATE_CHANGED)) // 재생상태 변경 전송
                            updateNotificationPlayer()
                        }
                    }
                    else -> {
                    }
                }

                isPlaying = state == PlayerConstants.PlayerState.PLAYING
            }

            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {

            }

            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {

            }

            override fun onVideoLoadedFraction(
                youTubePlayer: YouTubePlayer,
                loadedFraction: Float
            ) {

            }
        })
    }

    override fun onCreate() {
        super.onCreate()
        mNotificationPlayer = NotificationPlayer(this)
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            CommandActions.TOGGLE_PLAY -> {
                if (isPlaying) {
                    pause()
                } else {
                    play()
                }
            }
            CommandActions.REWIND -> {
                rewind()
            }
            CommandActions.FORWARD -> {
                forward()
            }
            CommandActions.CLOSE -> {
                pause()
                removeNotificationPlayer()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer?.let {
            it.pause()
            mMediaPlayer = null
        }
        removeNotificationPlayer()
    }

    private fun updateNotificationPlayer() {
        mNotificationPlayer?.updateNotificationPlayer()
    }

    private fun removeNotificationPlayer() {
        mNotificationPlayer?.removeNotificationPlayer()
    }

    fun setPlayList(musics: ArrayList<MusicListModel.MusicModel>) {
        mMusics.clear()
        mMusics.addAll(musics)
        play(0)
    }

    fun play(position: Int) {
        mCurrentPosition = position
        val id = mMusics[position].snippet.resourceId.videoId
        mMediaPlayer?.loadVideo(id, 0f)
    }

    fun play() {
        if (isPrepared) {
            mMediaPlayer?.let {
                it.play()
                sendBroadcast(Intent(BroadcastActions.PLAY_STATE_CHANGED)) // 재생상태 변경 전송
                updateNotificationPlayer()
            }
        }
    }

    fun pause() {
        if (isPrepared) {
            mMediaPlayer?.let {
                it.pause()
                sendBroadcast(Intent(BroadcastActions.PLAY_STATE_CHANGED)) // 재생상태 변경 전송
                updateNotificationPlayer()
            }
        }
    }

    fun forward() {
        if (mMusics.size - 1 > mCurrentPosition) {
            mCurrentPosition++ // 다음 포지션으로 이동.
        } else {
            mCurrentPosition = 0 // 처음 포지션으로 이동.
        }
        play(mCurrentPosition)
    }

    fun rewind() {
        if (mCurrentPosition > 0) {
            mCurrentPosition-- // 이전 포지션으로 이동.
        } else {
            mCurrentPosition = mMusics.size - 1 // 마지막 포지션으로 이동.
        }
        play(mCurrentPosition)
    }
}
