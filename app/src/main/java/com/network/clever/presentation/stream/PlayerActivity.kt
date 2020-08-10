package com.network.clever.presentation.stream

import android.os.Bundle
import com.network.clever.R
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.presentation.BaseActivity
import com.network.clever.presentation.Caller
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
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

//        videoUrl = String.format(
//            AppConfig.youtubePlayerUrl,
//            music.snippet.resourceId.videoId
//        )
//         https://music.youtube.com/watch?v=cMFHUTJ13Ys

//        extractYoutubeUrl(music.snippet.resourceId.videoId)

        /**
         * @ enableBackgroundPlayback(true)
         * Remember that this behavior is against YouTube terms of service,
         * therefore if you decide to allow background playback you won't be able to publish your app on the Play Store.
         */
        youtubePlayerView.enableBackgroundPlayback(true)
        youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback{
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(music.snippet.resourceId.videoId, 0f)

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

                    }

                    override fun onStateChange(
                        youTubePlayer: YouTubePlayer,
                        state: PlayerConstants.PlayerState
                    ) {

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
}