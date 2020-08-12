package com.network.clever.utility.player

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.network.clever.constant.BroadcastActions
import com.network.clever.constant.CommandActions
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.data.datasource.model.setting.AppSetting
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class AudioService : Service() {
    private val mBinder: IBinder = AudioServiceBinder()
    private val mMusics: ArrayList<MusicListModel.MusicModel> = ArrayList()
    private var mMediaPlayer: YouTubePlayer? = null
    private var mCurrentPosition = 0

    lateinit var youtubeView: YouTubePlayerView
    var mNotificationPlayer: NotificationPlayer? = null

    //    var isPlaying = false
    var playerState = PlayerConstants.PlayerState.UNSTARTED
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

    private fun setPlayer() {
        youtubeView.enableBackgroundPlayback(true)
        youtubeView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                mMediaPlayer = youTubePlayer

////                youTubePlayer.cueVideo(music.snippet.resourceId.videoId, 0f)
//                CleverPlayer.instance.serviceInterface.initNotification()
//                CleverPlayer.instance.serviceInterface.setPlayer(youTubePlayer)
//                CleverPlayer.instance.serviceInterface.setPlayList(music)

                youTubePlayer.addListener(object : YouTubePlayerListener {
                    override fun onApiChange(youTubePlayer: YouTubePlayer) {

                    }

                    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {

                    }

                    override fun onError(
                        youTubePlayer: YouTubePlayer,
                        error: PlayerConstants.PlayerError
                    ) {
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
                                }
                            }
                            else -> {
                            }
                        }

                        playerState = state

                        sendBroadcast(Intent(BroadcastActions.PLAY_STATE_CHANGED)) // 재생상태 변경 전송
                        updateNotificationPlayer()
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
        })
    }

    override fun onCreate() {
        super.onCreate()

        youtubeView = YouTubePlayerView(this)
        setPlayer()
//        if (appSetting.isBackgroundPlay)
        mNotificationPlayer = NotificationPlayer(this)
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            CommandActions.TOGGLE_PLAY -> {
                if (playerState == PlayerConstants.PlayerState.PLAYING) {
                    pause()
                } else if (playerState == PlayerConstants.PlayerState.PAUSED) {
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
        if (appSetting.isBackgroundPlay)
            mNotificationPlayer?.updateNotificationPlayer()
    }

    private fun removeNotificationPlayer() {
        if (appSetting.isBackgroundPlay)
            mNotificationPlayer?.removeNotificationPlayer()
    }

    private var appSetting = AppSetting(isRepeatChecked = false, isBackgroundPlay = false)
    fun setLocalAppSetting(setting: AppSetting) {
        appSetting = setting
    }

    fun setPlayList(musics: ArrayList<MusicListModel.MusicModel>, videoId: String) {
        mMusics.clear()
        mMusics.addAll(musics)

        val position = mMusics.indexOfFirst {
            it.snippet.resourceId.videoId == videoId
        }

        if (mCurrentPosition != position)
            play(position)
    }

    fun play(position: Int) {
        mCurrentPosition = position
        val id = mMusics[position].snippet.resourceId.videoId
        mMediaPlayer?.cueVideo(id, 0f)
        play()
    }

    fun play() {
        mMediaPlayer?.let {
            it.play()
            updateNotificationPlayer()
        }
    }

    fun pause() {
        mMediaPlayer?.let {
            it.pause()
            updateNotificationPlayer()
        }
    }

    fun forward() {
        if (mMusics.size - 1 > mCurrentPosition) {
            mCurrentPosition++ // 다음 포지션으로 이동.
        } else {
            mCurrentPosition = 0 // 처음 포지션으로 이동.
        }

        if (appSetting.isRepeatChecked)
            play(mCurrentPosition)
        else
            removeNotificationPlayer()
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
