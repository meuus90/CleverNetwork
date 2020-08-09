package com.network.clever.presentation.stream

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.meuus.base.utility.Params
import com.meuus.base.utility.Query
import com.meuus.base.view.AutoClearedValue
import com.network.clever.R
import com.network.clever.constant.AppConfig
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.domain.viewmodel.item.MyPlaylistViewModel
import com.network.clever.presentation.BaseFragment
import com.network.clever.presentation.stream.player.PlayerManager
import kotlinx.android.synthetic.main.fragment_player.*
import javax.inject.Inject

class PlayerFragment : BaseFragment() {
    companion object {
        fun newInstance() = PlayerFragment().apply {
            arguments = Bundle(1).apply {
                putString(FRAGMENT_TAG, PlayerFragment::class.java.name)
            }
        }
    }

    @Inject
    internal lateinit var myPlaylistViewModel: MyPlaylistViewModel

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
        return acvView.get()?.rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playerManager = PlayerManager.with(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initInstances()
    }

    private fun getPlaylist() {
        val query = Query.query(listOf())

        myPlaylistViewModel.getMutableList(Params(query))
        myPlaylistViewModel.playlist.observe(viewLifecycleOwner, Observer { resource ->
            val list = resource as PagedList<MusicListModel.MusicModel>
        })
    }

    lateinit var music: MusicListModel.MusicModel

    private fun initInstances() {
        if (PlayerManager.getService() == null) {
            playerManager.bind()
        }

        video_view.controllerHideOnTouch = false
        video_view.controllerShowTimeoutMs = 0
        video_view.showController()

    }

    private fun managerBinding() {
        val videoUrl = String.format(
            AppConfig.youtubePlayerUrl,
            music.snippet.resourceId.videoId
        )

        if (playerManager.isServiceBound) {
            playerManager.playOrPause(videoUrl)
            video_view.player = PlayerManager.getService().exoPlayer
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
        video_view.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        video_view.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
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