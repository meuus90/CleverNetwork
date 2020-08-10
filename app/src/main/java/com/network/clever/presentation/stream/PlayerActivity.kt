package com.network.clever.presentation.stream

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import com.network.clever.AudioApplication
import com.network.clever.R
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.presentation.BaseActivity
import com.network.clever.presentation.Caller
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
        updateUI()


//        youtubePlayerView.enableBackgroundPlayback(true)
//        youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
//            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
//                youTubePlayer.cueVideo(music.snippet.resourceId.videoId, 0f)
//
//                youTubePlayer.addListener(object : YouTubePlayerListener {
//                    override fun onApiChange(youTubePlayer: YouTubePlayer) {
//
//                    }
//
//                    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
//
//                    }
//
//                    override fun onError(
//                        youTubePlayer: YouTubePlayer,
//                        error: PlayerConstants.PlayerError
//                    ) {
//
//                    }
//
//                    override fun onPlaybackQualityChange(
//                        youTubePlayer: YouTubePlayer,
//                        playbackQuality: PlayerConstants.PlaybackQuality
//                    ) {
//
//                    }
//
//                    override fun onPlaybackRateChange(
//                        youTubePlayer: YouTubePlayer,
//                        playbackRate: PlayerConstants.PlaybackRate
//                    ) {
//
//                    }
//
//                    override fun onReady(youTubePlayer: YouTubePlayer) {
//
//                    }
//
//                    override fun onStateChange(
//                        youTubePlayer: YouTubePlayer,
//                        state: PlayerConstants.PlayerState
//                    ) {
//
//                    }
//
//                    override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
//
//                    }
//
//                    override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
//
//                    }
//
//                    override fun onVideoLoadedFraction(
//                        youTubePlayer: YouTubePlayer,
//                        loadedFraction: Float
//                    ) {
//
//                    }
//                })
//            }
//        })
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
        if (AudioApplication.instance.serviceInterface.isPlaying) {
            btn_play_pause.setImageResource(R.drawable.ic_pause_black)
        } else {
            btn_play_pause.setImageResource(R.drawable.ic_play_arrow_black)
        }
        val audioItem: MusicListModel.MusicModel? =
            AudioApplication.instance.serviceInterface.audioItem
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.btn_rewind ->                 // 이전곡으로 이동
                AudioApplication.instance.serviceInterface.rewind()
            R.id.btn_play_pause ->                 // 재생 또는 일시정지
                AudioApplication.instance.serviceInterface.togglePlay()
            R.id.btn_forward ->                 // 다음곡으로 이동
                AudioApplication.instance.serviceInterface.forward()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterBroadcast()
    }
}