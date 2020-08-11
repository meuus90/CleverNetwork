package com.network.clever.presentation.stream

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import com.network.clever.CleverPlayer
import com.network.clever.R
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.presentation.BaseActivity
import com.network.clever.presentation.Caller
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import kotlinx.android.synthetic.main.fragment_player.*


class PlayerActivity : BaseActivity() {
    override val frameLayoutId = R.id.contentFrame

    override fun setContentView() {
        setContentView(R.layout.fragment_player)
    }

    lateinit var music: MusicListModel.MusicModel
    private var videoUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        music = intent.getParcelableExtra(Caller.KEY_MUSIC)

        registerBroadcast()
        youtubePlayerView.enableBackgroundPlayback(true)
        youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
//                youTubePlayer.cueVideo(music.snippet.resourceId.videoId, 0f)
                CleverPlayer.instance.serviceInterface.setPlayer(youTubePlayer)
                CleverPlayer.instance.serviceInterface.setPlayList(arrayListOf(music))
            }
        })
        updateUI()
    }


    private val mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateUI()
        }
    }

    private fun registerBroadcast() {
        val filter = IntentFilter()
        filter.addAction(BroadcastActions.PREPARED)
        filter.addAction(BroadcastActions.PLAY_STATE_CHANGED)
        registerReceiver(mBroadcastReceiver, filter)
    }

    private fun unregisterBroadcast() {
        unregisterReceiver(mBroadcastReceiver)
    }

    private fun updateUI() {
        if (CleverPlayer.instance.serviceInterface.isPlaying) {
            btn_play_pause.setImageResource(R.drawable.ic_pause_black)
        } else {
            btn_play_pause.setImageResource(R.drawable.ic_play_arrow_black)
        }
        val audioItem: MusicListModel.MusicModel? =
            CleverPlayer.instance.serviceInterface.audioItem
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.btn_rewind ->                 // 이전곡으로 이동
                CleverPlayer.instance.serviceInterface.rewind()
            R.id.btn_play_pause ->                 // 재생 또는 일시정지
                CleverPlayer.instance.serviceInterface.togglePlay()
            R.id.btn_forward ->                 // 다음곡으로 이동
                CleverPlayer.instance.serviceInterface.forward()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterBroadcast()
    }
}