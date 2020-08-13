package com.network.clever.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.media.MediaPlayer
import android.media.session.MediaController
import android.media.session.MediaSession
import android.os.Binder
import android.os.Build
import android.os.IBinder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.network.clever.R
import com.network.clever.constant.CommandActions
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.data.datasource.model.setting.AppSetting
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import javax.annotation.Nullable


open class MediaPlayerService : Service() {
    private var mMediaPlayer: MediaPlayer? = null
    private lateinit var mSession: MediaSession
    private lateinit var mController: MediaController

    private val mBinder: IBinder = MediaPlayerServiceBinder()
    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    inner class MediaPlayerServiceBinder : Binder() {
        val service: MediaPlayerService = this@MediaPlayerService
    }

    override fun onCreate() {
        super.onCreate()

        initYoutubePlayer()
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null || intent.action == null) return
        val action = intent.action
        when {
            action.equals(CommandActions.PLAY, ignoreCase = true) -> {
                mController.transportControls.play()
            }
            action.equals(CommandActions.PAUSE, ignoreCase = true) -> {
                mController.transportControls.pause()
            }
            action.equals(CommandActions.FORWARD, ignoreCase = true) -> {
                mController.transportControls.fastForward()
            }
            action.equals(CommandActions.REWIND, ignoreCase = true) -> {
                mController.transportControls.rewind()
            }
        }
    }

    private fun generateAction(
        icon: Int,
        title: String,
        intentAction: String
    ): Notification.Action {
        val intent = Intent(
            applicationContext,
            MediaPlayerService::class.java
        )
        intent.action = intentAction
        val pendingIntent = PendingIntent.getService(
            applicationContext,
            1,
            intent,
            0
        )
        return Notification.Action.Builder(
            Icon.createWithResource(
                "",
                icon
            ), title, pendingIntent
        ).build()
    }

    private fun buildNotification(action: Notification.Action) {
        Glide.with(applicationContext)
            .asBitmap()
            .load(audioItem?.snippet?.thumbnails?.default?.url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    buildNotification(resource, action)
                }
            })
    }

    private fun buildNotification(icon: Bitmap, action: Notification.Action) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val appName = getString(R.string.app_name)
        val channelId = "$packageName-$appName"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, appName,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "CleverPlayer notification channel"
            channel.setShowBadge(false)

            notificationManager.createNotificationChannel(channel)
        }

        val style: Notification.MediaStyle = Notification.MediaStyle()
        val intent = Intent(
            applicationContext,
            MediaPlayerService::class.java
        )
        intent.action = CommandActions.STOP
        val pendingIntent = PendingIntent.getService(
            applicationContext, 1, intent, 0
        )
        val builder: Notification.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder(this, channelId)
            } else {
                Notification.Builder(this)
            }

        builder.setSmallIcon(R.drawable.ic_play_circle_filled_black)
            .setLargeIcon(icon)
            .setContentTitle(audioItem?.snippet?.title ?: "")
            .setContentText(audioItem?.snippet?.channelTitle ?: "")
            .setDeleteIntent(pendingIntent)
            .setStyle(style)

        builder.addAction(
            generateAction(
                R.drawable.ic_fast_rewind_black,
                "Rewind",
                CommandActions.REWIND
            )
        )
        builder.addAction(action)
        builder.addAction(
            generateAction(
                R.drawable.ic_fast_forward_black,
                "Fast Foward",
                CommandActions.FORWARD
            )
        )
        style.setShowActionsInCompactView(0, 1, 2)

        notificationManager.notify(1, builder.build())
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (mMediaPlayer == null) {
            initMediaSessions()
        }
        handleIntent(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    private var mYouTubePlayer: YouTubePlayer? = null
    private fun initYoutubePlayer() {
        val youtubeView = YouTubePlayerView(this)
        youtubeView.enableBackgroundPlayback(true)
        youtubeView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                mYouTubePlayer = youTubePlayer

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
//                        youTubePlayer.play()
                    }

                    override fun onStateChange(
                        youTubePlayer: YouTubePlayer,
                        state: PlayerConstants.PlayerState
                    ) {
                        when (state) {
                            PlayerConstants.PlayerState.ENDED -> {
                                forward()
                            }
                            else -> {
                            }
                        }

                        playerState = state
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

    private fun initMediaSessions() {
        mMediaPlayer = MediaPlayer()
        mSession = MediaSession(
            applicationContext,
            "simple player session"
        )
        mController = MediaController(applicationContext, mSession.sessionToken)
        mSession.setCallback(object : MediaSession.Callback() {
            override fun onPlay() {
                super.onPlay()
                play()
            }

            override fun onPause() {
                super.onPause()
                pause()
            }

            override fun onFastForward() {
                super.onFastForward()
                forward()
            }

            override fun onRewind() {
                super.onRewind()
                rewind()
            }

            override fun onStop() {
                super.onStop()
                stop()
            }
        }
        )
    }

    override fun onUnbind(intent: Intent): Boolean {
        mSession.release()
        return super.onUnbind(intent)
    }

    private val mMusics: ArrayList<MusicListModel.MusicModel> = ArrayList()
    private var mCurrentPosition = -1
    val audioItem: MusicListModel.MusicModel?
        get() = try {
            mMusics[mCurrentPosition]
        } catch (e: Exception) {
            null
        }

    private var mAppSetting = AppSetting(isRepeatChecked = false, isBackgroundPlay = false)
    fun setAppSetting(setting: AppSetting) {
        mAppSetting = setting
    }

    fun setPlayList(
        musics: ArrayList<MusicListModel.MusicModel>,
        videoId: String,
        setting: AppSetting
    ) {
        mAppSetting = setting

        mMusics.clear()
        mMusics.addAll(musics)

        var position = mMusics.indexOfFirst {
            it.snippet.resourceId.videoId == videoId
        }

        if (mCurrentPosition != position) {
            if (position == -1)
                position = 0
            play(position)
        }
    }

    open var updateNotificationPlayer = {}

    var playerState = PlayerConstants.PlayerState.UNSTARTED

    private fun play(position: Int) {
        mCurrentPosition = position
        val id = mMusics[position].snippet.resourceId.videoId
        mYouTubePlayer?.cueVideo(id, 0f)
        play()
    }

    fun play() {
        mYouTubePlayer?.play()
        updateNotificationPlayer()
        buildNotification(
            generateAction(
                R.drawable.ic_pause_black,
                "Pause",
                CommandActions.PAUSE
            )
        )
    }

    fun pause() {
        mYouTubePlayer?.pause()
        updateNotificationPlayer()
        buildNotification(
            generateAction(
                R.drawable.ic_play_arrow_black,
                "Play",
                CommandActions.PLAY
            )
        )
    }

    fun togglePlay() {
        if (playerState == PlayerConstants.PlayerState.PLAYING)
            pause()
        else
            play()
    }

    fun stop() {
        mYouTubePlayer?.pause()
        val notificationManager =
            applicationContext
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)
        val intent = Intent(
            applicationContext,
            MediaPlayerService::class.java
        )
        stopService(intent)
    }

    fun forward() {
        if (mCurrentPosition < mMusics.lastIndex) {
            mCurrentPosition++
            play(mCurrentPosition)
        } else {
            if (mAppSetting.isRepeatChecked) {
                mCurrentPosition = 0
                play(mCurrentPosition)
            } else
                stop()
        }
    }

    fun rewind() {
        if (mCurrentPosition > 0) {
            mCurrentPosition--
        } else {
            mCurrentPosition = mMusics.lastIndex
        }
        play(mCurrentPosition)
    }
}