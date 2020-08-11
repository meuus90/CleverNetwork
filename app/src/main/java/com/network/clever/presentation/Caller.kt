package com.network.clever.presentation

import android.content.Context
import android.content.Intent
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.data.datasource.model.item.PlaylistListModel
import com.network.clever.presentation.auth.AuthActivity
import com.network.clever.presentation.playlist.PlaylistActivity
import com.network.clever.presentation.stream.PlayerActivity
import com.network.clever.presentation.tab.HomeActivity

object Caller {
    const val TAB_TEY = "TAB_TEY"
    const val TAB_MY_LIST = 0
    const val TAB_LISTS = 1
    const val TAB_SETTING = 2

    const val KEY_PLAYLIST = "KEY_PLAYLIST"
    const val KEY_MUSIC = "KEY_MUSIC"

    internal fun logoutApp(context: Context) {
        val intent = Intent(context, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    internal fun openMyPlaylist(activity: BaseActivity) {
        val intent = Intent(activity, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(TAB_TEY, TAB_MY_LIST)
        }
        activity.startActivity(intent)
    }

    internal fun openList(activity: BaseActivity) {
        val intent = Intent(activity, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(TAB_TEY, TAB_LISTS)
        }
        activity.startActivity(intent)
    }

    internal fun openSetting(activity: BaseActivity) {
        val intent = Intent(activity, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(TAB_TEY, TAB_SETTING)
        }
        activity.startActivity(intent)
    }

    internal fun openPlaylist(activity: BaseActivity, playlist: PlaylistListModel.PlaylistModel) {
        val intent = Intent(activity, PlaylistActivity::class.java).apply {
            putExtra(KEY_PLAYLIST, playlist)
        }
        activity.startActivity(intent)
//        activity.overridePendingTransition(R.anim.slide_in_right_left, R.anim.slide_out_left_right)
    }

    internal fun openPlayer(activity: BaseActivity, music: ArrayList<MusicListModel.MusicModel>) {
        val intent = Intent(activity, PlayerActivity::class.java).apply {
            putExtra(KEY_MUSIC, music)
        }
        activity.startActivity(intent)
//        activity.overridePendingTransition(R.anim.slide_in_right_left, R.anim.slide_out_left_right)
    }
}