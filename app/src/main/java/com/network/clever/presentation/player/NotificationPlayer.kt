package com.network.clever.presentation.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.AppWidgetTarget
import com.network.clever.R
import com.network.clever.constant.CommandActions
import com.network.clever.presentation.tab.HomeActivity


class NotificationPlayer(service: AudioService) {
    private val mService: AudioService = service
    private val mNotificationManager: NotificationManager =
        service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private var mNotificationManagerBuilder: NotificationManagerBuilder? = null
    private var isForeground = false

    private var channelId = ""
    fun initNotificationPlayer() {
        cancel()

        channelId = createNotificationChannel(
            mService, NotificationManagerCompat.IMPORTANCE_DEFAULT,
            false, mService.getString(R.string.app_name), "App notification channel"
        ) // 1

        mNotificationManagerBuilder = NotificationManagerBuilder(channelId)
        mNotificationManagerBuilder?.execute()
    }

    fun updateNotificationPlayer() {
        cancel()

        mNotificationManagerBuilder = NotificationManagerBuilder(channelId)
        mNotificationManagerBuilder?.execute()
    }

    fun removeNotificationPlayer() {
        cancel()
        mService.stopForeground(true)
        isForeground = false
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

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)

            channelId
        } else ""
    }

    private fun cancel() {
        mNotificationManagerBuilder?.cancel(true)
        mNotificationManagerBuilder = null
    }

    private inner class NotificationManagerBuilder(val channelId: String) :
        AsyncTask<Any, Any, Notification>() {
        lateinit var mRemoteViews: RemoteViews
        lateinit var mNotificationBuilder: NotificationCompat.Builder
        lateinit var mMainPendingIntent: PendingIntent

        override fun onPreExecute() {
            super.onPreExecute()

            val mainActivity = Intent(mService, HomeActivity::class.java)
            mMainPendingIntent = PendingIntent.getActivity(mService, 0, mainActivity, 0)
            mRemoteViews = createRemoteView(R.layout.notification_player)
            mNotificationBuilder =
                NotificationCompat.Builder(mService, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true)
                    .setContentIntent(mMainPendingIntent)
                    .setContent(mRemoteViews)

            NotificationManagerCompat.from(mService)
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
            val remoteView = RemoteViews(mService.packageName, id)
            val actionTogglePlay = Intent(CommandActions.TOGGLE_PLAY)
            val actionForward = Intent(CommandActions.FORWARD)
            val actionRewind = Intent(CommandActions.REWIND)
            val actionClose = Intent(CommandActions.CLOSE)
            val togglePlay =
                PendingIntent.getService(mService, 0, actionTogglePlay, 0)
            val forward = PendingIntent.getService(mService, 0, Intent(CommandActions.FORWARD), 0)
            val rewind = PendingIntent.getService(mService, 0, Intent(CommandActions.REWIND), 0)
            val close = PendingIntent.getService(mService, 0, Intent(CommandActions.CLOSE), 0)
            remoteView.setOnClickPendingIntent(R.id.btn_play_pause, togglePlay)
            remoteView.setOnClickPendingIntent(R.id.btn_forward, forward)
            remoteView.setOnClickPendingIntent(R.id.btn_rewind, rewind)
            remoteView.setOnClickPendingIntent(R.id.btn_close, close)
            return remoteView
        }

        private fun updateRemoteView(remoteViews: RemoteViews) {
            val title = mService.audioItem?.snippet?.title
            val albumArtUrl = mService.audioItem?.snippet?.thumbnails?.default?.url

            remoteViews.apply {
                if (mService.isPlaying) {
                    setImageViewResource(R.id.btn_play_pause, R.drawable.ic_pause_black)
                } else {
                    setImageViewResource(
                        R.id.btn_play_pause,
                        R.drawable.ic_play_arrow_black
                    )
                }

                setTextViewText(R.id.txt_title, title)
                try {
                    val appWidgetTarget =
                        AppWidgetTarget(mService, R.id.img_albumart, this, NOTIFICATION_PLAYER_ID)
                    Glide.with(mService).asBitmap()
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