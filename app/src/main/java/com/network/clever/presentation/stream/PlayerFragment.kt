package com.network.clever.presentation.stream

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.ui.PlayerView
import com.meuus.base.view.AutoClearedValue
import com.network.clever.R
import com.network.clever.constant.AppConfig
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.presentation.BaseFragment
import com.network.clever.presentation.stream.player.PlayerManager

class PlayerFragment : BaseFragment() {
    companion object {
        fun newInstance() = PlayerFragment().apply {
            arguments = Bundle(1).apply {
                putString(FRAGMENT_TAG, PlayerFragment::class.java.name)
            }
        }
    }

    lateinit var playerManager: PlayerManager

    private val playerActivity: PlayerActivity by lazy {
        activity as PlayerActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val acvView =
            AutoClearedValue(
                this,
                inflater.inflate(R.layout.fragment_player, container, false)
            )
        acvView.get()?.rootView?.let {
            initInstances(it)
        }
        return acvView.get()?.rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playerManager = PlayerManager.with(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }


    private var playerView: PlayerView? = null

    lateinit var music: MusicListModel.MusicModel

    private fun initInstances(rootView: View) {
        playerView = rootView.findViewById(R.id.video_view)

        if (PlayerManager.getService() == null) {
            playerManager.bind()
        }

        playerView!!.controllerHideOnTouch = false
        playerView!!.controllerShowTimeoutMs = 0
        playerView!!.showController()

    }

    private fun managerBinding() {
        val videoUrl = String.format(
            AppConfig.youtubePlayerUrl,
            music.snippet.resourceId.videoId,
            music.snippet.playlistId
        )

        if (playerManager.isServiceBound) {
            playerManager.playOrPause(videoUrl)
            playerView!!.player = PlayerManager.getService().exoPlayer
        }
    }

    override fun onResume() {
        super.onResume()

        managerBinding()
    }

    override fun onDestroy() {
        playerManager.unbind()

        super.onDestroy()
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUiFullScreen() {
        playerView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        playerView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val currentOrientation = resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUiFullScreen()
        } else {
            hideSystemUi()
        }
    }
}