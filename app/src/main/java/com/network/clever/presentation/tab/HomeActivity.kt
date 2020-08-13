package com.network.clever.presentation.tab

import android.os.Bundle
import com.network.clever.R
import com.network.clever.presentation.BaseActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import kotlinx.android.synthetic.main.activity_home.*
import timber.log.Timber


class HomeActivity : BaseActivity() {
    override val frameLayoutId = R.id.contentFrame

    override fun setContentView() {
        setContentView(R.layout.activity_home)
    }

    override fun onUpdateUI() {
        super.onUpdateUI()
        setUI()
    }

    private fun setUI() {
        try {
            when (audioService?.playerState) {
                PlayerConstants.PlayerState.PLAYING -> {
                    btn_play_pause.setImageResource(R.drawable.ic_pause_black)
                }
                PlayerConstants.PlayerState.PAUSED -> {
                    btn_play_pause.setImageResource(R.drawable.ic_play_arrow_black)
                }
                else -> {
                }
            }

            audioService?.audioItem?.let {
                tv_title.text = it.snippet.title
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        fragment.setUI()
    }

    lateinit var fragment: TabFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragment = addFragment(
            TabFragment::class.java,
            BACK_STACK_STATE_NEW
        ) as TabFragment
        setUI()

        v_mini_player.setOnClickListener {
            playerDialog.show(supportFragmentManager, null)
        }

        btn_rewind.setOnClickListener {
            audioService?.rewind()
        }
        btn_play_pause.setOnClickListener {
            audioService?.togglePlay()
        }
        btn_forward.setOnClickListener {
            audioService?.forward()
        }
    }
}