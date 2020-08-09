package com.network.clever.presentation.stream

import android.os.Bundle
import com.network.clever.R
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.presentation.BaseActivity
import com.network.clever.presentation.Caller


class PlayerActivity : BaseActivity() {
    override val frameLayoutId = R.id.contentFrame

    override fun setContentView() {
        setContentView(R.layout.activity_default)
    }

    lateinit var music: MusicListModel.MusicModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        music = intent.getParcelableExtra(Caller.KEY_MUSIC)

        val fragment = addFragment(
            PlayerFragment::class.java,
            BACK_STACK_STATE_NEW
        )

        (fragment as PlayerFragment).music = music
    }
}