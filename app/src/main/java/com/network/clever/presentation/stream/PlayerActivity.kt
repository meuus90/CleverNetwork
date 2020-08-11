package com.network.clever.presentation.stream

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.bumptech.glide.Glide
import com.network.clever.CleverPlayer
import com.network.clever.R
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.presentation.BaseActivity
import com.network.clever.presentation.Caller
import kotlinx.android.synthetic.main.fragment_player.*
import timber.log.Timber


class PlayerActivity : BaseActivity() {
    override val frameLayoutId = R.id.contentFrame

    override fun setContentView() {
        setContentView(R.layout.fragment_player)
    }

    lateinit var music: ArrayList<MusicListModel.MusicModel>
    private var videoUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        music = intent.getParcelableArrayListExtra<MusicListModel.MusicModel>(Caller.KEY_MUSIC)

        registerBroadcast()

        CleverPlayer.instance.serviceInterface.setPlayList(music)

        btn_rewind.setOnClickListener {
            CleverPlayer.instance.serviceInterface.rewind()
        }
        btn_play_pause.setOnClickListener {
            CleverPlayer.instance.serviceInterface.togglePlay()
        }
        btn_forward.setOnClickListener {
            CleverPlayer.instance.serviceInterface.forward()
        }
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
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterBroadcast()
    }
}