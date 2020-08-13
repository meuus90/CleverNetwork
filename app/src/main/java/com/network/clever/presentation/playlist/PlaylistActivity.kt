package com.network.clever.presentation.playlist

import android.os.Bundle
import com.network.clever.R
import com.network.clever.data.datasource.model.item.PlaylistListModel
import com.network.clever.presentation.BaseActivity
import com.network.clever.presentation.Caller


class PlaylistActivity : BaseActivity() {
    override val frameLayoutId = R.id.contentFrame

    override fun setContentView() {
        setContentView(R.layout.activity_default)
    }

    override fun onUpdateUI() {
        super.onUpdateUI()
    }

    lateinit var playlist: PlaylistListModel.PlaylistModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playlist = intent.getParcelableExtra(Caller.KEY_PLAYLIST)

        addFragment(
            PlaylistFragment::class.java,
            BACK_STACK_STATE_NEW
        )
    }
}