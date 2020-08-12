package com.network.clever.utility.player

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.AppWidgetTarget
import com.network.clever.R
import com.network.clever.constant.CommandActions
import com.network.clever.presentation.tab.HomeActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants


class NotificationPlayer(val service: AudioService) {
    private lateinit var mNotificationManager: NotificationManager
    private var mNotificationManagerBuilder: NotificationManagerBuilder
    private var channelId: String

    init {
        channelId = createNotificationChannel(
            service, NotificationManagerCompat.IMPORTANCE_DEFAULT,
            false, service.getString(R.string.app_name), "App notification channel"
        )

        mNotificationManagerBuilder = NotificationManagerBuilder(channelId)
    }

    private fun cancel() {
        mNotificationManagerBuilder.cancel(true)
    }

    fun updateNotificationPlayer() {
        cancel()

        mNotificationManagerBuilder = NotificationManagerBuilder(channelId)
        mNotificationManagerBuilder.execute()
    }

    fun removeNotificationPlayer() {
        cancel()
        service.stopForeground(true)
    }

    private fun createNotificationChannel(
        context: Context, importance: Int, showBadge: Boolean,
        name: String, description: String
    ): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            mNotificationManager = context.getSystemService(NotificationManager::class.java)
            mNotificationManager.createNotificationChannel(channel)

            Log.e("NotificationPlayer", "Notification created")

            channelId
        } else ""
    }

    @SuppressLint("StaticFieldLeak")
    private inner class NotificationManagerBuilder(val channelId: String) :
        AsyncTask<Any, Any, Notification>() {
        lateinit var mRemoteViews: RemoteViews
        lateinit var mNotificationBuilder: NotificationCompat.Builder
        lateinit var mMainPendingIntent: PendingIntent

        override fun onPreExecute() {
            super.onPreExecute()

            val mainActivity = Intent(service, HomeActivity::class.java)
            mMainPendingIntent = PendingIntent.getActivity(service, 0, mainActivity, 0)
            mRemoteViews = createRemoteView(R.layout.notification_player)
            mNotificationBuilder =
                NotificationCompat.Builder(service, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true)
                    .setContentIntent(mMainPendingIntent)
                    .setContent(mRemoteViews)

            NotificationManagerCompat.from(service)
                .notify(NOTIFICATION_PLAYER_ID, mNotificationBuilder.build())
        }

        override fun doInBackground(params: Array<Any>): Notification {
            return mNotificationBuilder.apply {
                setContent(mRemoteViews)
                setContentIntent(mMainPendingIntent)
                priority = Notification.PRIORITY_MAX
                setVibrate(null)
                setSound(null)
                updateRemoteView(mRemoteViews)
            }.build()
        }

        override fun onPostExecute(notification: Notification) {
            super.onPostExecute(notification)
            try {
                mNotificationManager.notify(NOTIFICATION_PLAYER_ID, notification)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun createRemoteView(id: Int): RemoteViews {
            val remoteView = RemoteViews(service.packageName, id)
            val actionTogglePlay = Intent(CommandActions.TOGGLE_PLAY)
            val actionForward = Intent(CommandActions.FORWARD)
            val actionRewind = Intent(CommandActions.REWIND)
            val actionClose = Intent(CommandActions.CLOSE)
            val togglePlay =
                PendingIntent.getService(service, 0, actionTogglePlay, 0)
            val forward = PendingIntent.getService(service, 0, Intent(CommandActions.FORWARD), 0)
            val rewind = PendingIntent.getService(service, 0, Intent(CommandActions.REWIND), 0)
            val close = PendingIntent.getService(service, 0, Intent(CommandActions.CLOSE), 0)
            remoteView.setOnClickPendingIntent(R.id.btn_play_pause, togglePlay)
            remoteView.setOnClickPendingIntent(R.id.btn_forward, forward)
            remoteView.setOnClickPendingIntent(R.id.btn_rewind, rewind)
            remoteView.setOnClickPendingIntent(R.id.btn_close, close)
            return remoteView
        }

        private fun updateRemoteView(remoteViews: RemoteViews) {
            val title = service.audioItem?.snippet?.title
            val albumArtUrl = service.audioItem?.snippet?.thumbnails?.default?.url

            remoteViews.apply {
                when (service.playerState) {
                    PlayerConstants.PlayerState.PLAYING -> {
                        setViewVisibility(R.id.pb_loading, View.GONE)
                        setViewVisibility(R.id.btn_play_pause, View.VISIBLE)
                        setImageViewResource(R.id.btn_play_pause, R.drawable.ic_pause_black)
                    }
                    PlayerConstants.PlayerState.PAUSED -> {
                        setViewVisibility(R.id.pb_loading, View.GONE)
                        setViewVisibility(R.id.btn_play_pause, View.VISIBLE)
                        setImageViewResource(
                            R.id.btn_play_pause,
                            R.drawable.ic_play_arrow_black
                        )
                    }
                    else -> {
                        setViewVisibility(R.id.btn_play_pause, View.GONE)
                        setViewVisibility(R.id.pb_loading, View.VISIBLE)
                    }
                }

                setTextViewText(R.id.txt_title, title)
                try {
                    val appWidgetTarget =
                        AppWidgetTarget(service, R.id.img_albumart, this, NOTIFICATION_PLAYER_ID)
                    Glide.with(service).asBitmap()
                        .load(albumArtUrl)
                        .centerCrop()
                        .dontAnimate()
                        .into(appWidgetTarget)
                } catch (e: Exception) {
                }
            }
        }
    }

    companion object {
        private const val NOTIFICATION_PLAYER_ID = 0x342
    }

}