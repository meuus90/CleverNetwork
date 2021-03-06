package com.network.clever.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.meuus.base.view.gone
import com.meuus.base.view.setDefaultWindowTheme
import com.meuus.base.view.show
import com.network.clever.R
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.presentation.BaseActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import kotlinx.android.synthetic.main.fragment_player.*
import timber.log.Timber

class PlayerDialog(private val activity: BaseActivity) : DialogFragment() {
    override fun onStart() {
        super.onStart()
        dialog?.setDefaultWindowTheme()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return activity.layoutInflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_rewind.setOnClickListener {
            activity.audioService?.rewind()
        }
        btn_play_pause.setOnClickListener {
            activity.audioService?.togglePlay()
        }
        btn_forward.setOnClickListener {
            activity.audioService?.forward()
        }

        val audioItem: MusicListModel.MusicModel? =
            activity.audioService?.audioItem

        try {
            audioItem?.let {
                Glide.with(this).asDrawable().clone()
                    .load(it.snippet.thumbnails.maxres.url)
                    .centerCrop()
                    .dontAnimate()
                    .into(iv_thumbnail)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

        updateUI(activity.audioService?.playerState)
    }

    fun updateUI(playerState: PlayerConstants.PlayerState? = PlayerConstants.PlayerState.UNSTARTED) {
        when (playerState) {
            PlayerConstants.PlayerState.PLAYING -> {
                btn_play_pause.show()
                pb_loading.gone()
                btn_play_pause.setImageResource(R.drawable.ic_pause_black)
            }
            PlayerConstants.PlayerState.PAUSED -> {
                btn_play_pause.show()
                pb_loading.gone()
                btn_play_pause.setImageResource(R.drawable.ic_play_arrow_black)
            }
            else -> {
                btn_play_pause.gone()
                pb_loading.show()
//                btn_play_pause.show()
//                pb_loading.gone()
            }
        }

        val audioItem: MusicListModel.MusicModel? =
            activity.audioService?.audioItem

        try {
            audioItem?.let {
                Glide.with(this).asDrawable().clone()
                    .load(it.snippet.thumbnails.maxres.url)
                    .centerCrop()
                    .dontAnimate()
                    .into(iv_thumbnail)

                tv_title.text = it.snippet.title
                tv_author.text = it.snippet.channelTitle
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}