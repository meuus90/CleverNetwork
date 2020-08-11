package com.network.clever.presentation.stream

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.network.clever.R
import com.network.clever.presentation.tab.HomeActivity
import com.squareup.picasso.Picasso


class NotificationPlayer(service: AudioService) {
    private val mService: AudioService = service
    private val mNotificationManager: NotificationManager =
        service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private var mNotificationManagerBuilder: NotificationManagerBuilder? = null
    private var isForeground = false
    fun updateNotificationPlayer() {
        cancel()
        mNotificationManagerBuilder = NotificationManagerBuilder()
        mNotificationManagerBuilder!!.execute()
    }

    fun removeNotificationPlayer() {
        cancel()
        mService.stopForeground(true)
        isForeground = false
    }

    private fun cancel() {
        mNotificationManagerBuilder?.cancel(true)
        mNotificationManagerBuilder = null
    }

    @SuppressLint("StaticFieldLeak")
    private inner class NotificationManagerBuilder : AsyncTask<Any?, Any?, Notification>() {
        private var mRemoteViews: RemoteViews? = null
        private var mNotificationBuilder: NotificationCompat.Builder? = null
        private var mMainPendingIntent: PendingIntent? = null
        override fun onPreExecute() {
            super.onPreExecute()
            val mainActivity = Intent(mService, HomeActivity::class.java)
            mMainPendingIntent = PendingIntent.getActivity(mService, 0, mainActivity, 0)
            mRemoteViews = createRemoteView(R.layout.notification_player)
            mNotificationBuilder = NotificationCompat.Builder(mService)
            mNotificationBuilder?.let { builder ->
                builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true)
                    .setContentIntent(mMainPendingIntent)
                    .setContent(mRemoteViews)

                val notification: Notification = builder.build()
                notification.priority = Notification.PRIORITY_MAX
                notification.contentIntent = mMainPendingIntent
                if (!isForeground) {
                    isForeground = true
                    // 서비스를 Foreground 상태로 만든다
                    mService.startForeground(
                        NOTIFICATION_PLAYER_ID,
                        notification
                    )
                }
            }
        }

        override fun doInBackground(params: Array<Any?>): Notification? {
            mNotificationBuilder?.setContent(mRemoteViews)
            mNotificationBuilder?.setContentIntent(mMainPendingIntent)
            mNotificationBuilder?.priority = Notification.PRIORITY_MAX
            val notification: Notification? = mNotificationBuilder?.build()
            notification?.let {
                updateRemoteView(mRemoteViews, it)
            }
            return notification
        }

        override fun onPostExecute(notification: Notification?) {
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
            val forward = PendingIntent.getService(mService, 0, actionForward, 0)
            val rewind = PendingIntent.getService(mService, 0, actionRewind, 0)
            val close = PendingIntent.getService(mService, 0, actionClose, 0)
            remoteView.setOnClickPendingIntent(R.id.btn_play_pause, togglePlay)
            remoteView.setOnClickPendingIntent(R.id.btn_forward, forward)
            remoteView.setOnClickPendingIntent(R.id.btn_rewind, rewind)
            remoteView.setOnClickPendingIntent(R.id.btn_close, close)
            return remoteView
        }

        private fun updateRemoteView(
            remoteViews: RemoteViews?,
            notification: Notification
        ) {
            if (mService.isPlaying) {
                remoteViews?.setImageViewResource(R.id.btn_play_pause, R.drawable.ic_pause_black)
            } else {
                remoteViews?.setImageViewResource(
                    R.id.btn_play_pause,
                    R.drawable.ic_play_arrow_black
                )
            }
            val title = mService.audioItem?.snippet?.title
            remoteViews?.setTextViewText(R.id.txt_title, title)
            val albumArtUrl = mService.audioItem?.snippet?.thumbnails?.default?.url
            remoteViews?.let {
                Picasso.get().load(albumArtUrl)
                    .error(R.drawable.ic_launcher).into(
                        it,
                        R.id.img_albumart,
                        NOTIFICATION_PLAYER_ID,
                        notification
                    )
            }
        }
    }

    companion object {
        private const val NOTIFICATION_PLAYER_ID = 0x342
    }

}