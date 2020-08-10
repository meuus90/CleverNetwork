package com.network.clever.presentation.stream

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.provider.MediaStore
import com.network.clever.data.datasource.model.item.MusicListModel


class AudioService : Service() {
    private val mBinder: IBinder = AudioServiceBinder()
    private val mAudioIds: ArrayList<Long> = ArrayList()
    private var mMediaPlayer: MediaPlayer? = null
    private var isPrepared = false
    private var mCurrentPosition = 0
    private var mAudioItem: MusicListModel.MusicModel? = null
    private var mNotificationPlayer: NotificationPlayer? = null

    inner class AudioServiceBinder : Binder() {
        val service: AudioService
            get() = this@AudioService
    }

    override fun onCreate() {
        super.onCreate()
        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.setWakeMode(
            applicationContext,
            PowerManager.PARTIAL_WAKE_LOCK
        )
        mMediaPlayer?.setOnPreparedListener { mp ->
            isPrepared = true
            mp.start()
            sendBroadcast(Intent(BroadcastActions.PREPARED)) // prepared 전송
            updateNotificationPlayer()
        }
        mMediaPlayer?.setOnCompletionListener {
            isPrepared = false
            sendBroadcast(Intent(BroadcastActions.PLAY_STATE_CHANGED)) // 재생상태 변경 전송
            updateNotificationPlayer()
        }
        mMediaPlayer?.setOnErrorListener { mp, what, extra ->
            isPrepared = false
            sendBroadcast(Intent(BroadcastActions.PLAY_STATE_CHANGED)) // 재생상태 변경 전송
            updateNotificationPlayer()
            false
        }
        mMediaPlayer?.setOnSeekCompleteListener { }
        mNotificationPlayer = NotificationPlayer(this)
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            when (action) {
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
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
        removeNotificationPlayer()
    }

    private fun updateNotificationPlayer() {
        if (mNotificationPlayer != null) {
            mNotificationPlayer!!.updateNotificationPlayer()
        }
    }

    private fun removeNotificationPlayer() {
        if (mNotificationPlayer != null) {
            mNotificationPlayer!!.removeNotificationPlayer()
        }
    }

    @SuppressLint("Recycle")
    private fun queryAudioItem(position: Int) {
        mCurrentPosition = position
        val audioId = mAudioIds[position]
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )
        val selection = MediaStore.Audio.Media._ID + " = ?"
        val selectionArgs = arrayOf(audioId.toString())
//        contentResolver.query(uri, projection, selection, selectionArgs, null)?.let { cursor ->
//            if (cursor.count > 0) {
//                cursor.moveToFirst()
//                mAudioItem = AudioAdapter.AudioItem.bindCursor(cursor)
//            }
//            cursor.close()
//        }
    }

    private fun prepare() {
//        try {
//            mMediaPlayer?.let { player ->
//                player.setDataSource(mAudioItem?.mDataPath)
//                player.setAudioStreamType(AudioManager.STREAM_MUSIC)
//                player.prepareAsync()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    private fun stop() {
        mMediaPlayer!!.stop()
        mMediaPlayer!!.reset()
    }

    fun setPlayList(audioIds: ArrayList<Long>) {
        if (mAudioIds != audioIds) {
            mAudioIds.clear()
            mAudioIds.addAll(audioIds)
        }
    }

    val audioItem: MusicListModel.MusicModel?
        get() = mAudioItem

    val isPlaying: Boolean
        get() = mMediaPlayer?.isPlaying ?: false

    fun play(position: Int) {
        queryAudioItem(position)
        stop()
        prepare()
    }

    fun play() {
        if (isPrepared) {
            mMediaPlayer!!.start()
            sendBroadcast(Intent(BroadcastActions.PLAY_STATE_CHANGED)) // 재생상태 변경 전송
            updateNotificationPlayer()
        }
    }

    fun pause() {
        if (isPrepared) {
            mMediaPlayer!!.pause()
            sendBroadcast(Intent(BroadcastActions.PLAY_STATE_CHANGED)) // 재생상태 변경 전송
            updateNotificationPlayer()
        }
    }

    fun forward() {
        if (mAudioIds.size - 1 > mCurrentPosition) {
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
            mCurrentPosition = mAudioIds.size - 1 // 마지막 포지션으로 이동.
        }
        play(mCurrentPosition)
    }
}
