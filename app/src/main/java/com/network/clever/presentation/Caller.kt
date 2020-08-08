package com.network.clever.presentation

import android.content.Intent
import com.network.clever.R
import com.network.clever.data.datasource.model.item.PlaylistListModel
import com.network.clever.presentation.home.HomeActivity
import com.network.clever.presentation.stream.PlaylistActivity

object Caller {
    const val PLAYLIST_KEY = "PLAYLIST_KEY"

    internal fun openHome(activity: BaseActivity) {
        val intent = Intent(activity, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.slide_in_right_left, R.anim.slide_out_left_right)
    }

    internal fun openPlaylist(activity: BaseActivity, playlist: PlaylistListModel.PlaylistModel) {
        val intent = Intent(activity, PlaylistActivity::class.java).apply {
            putExtra(PLAYLIST_KEY, playlist)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.slide_in_right_left, R.anim.slide_out_left_right)
    }
}