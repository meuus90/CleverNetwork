package com.network.clever.presentation.playlist.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.network.clever.R
import com.network.clever.data.datasource.model.item.MusicListModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_music.*
import timber.log.Timber

class PlaylistAdapter(
    val context: Context,
    val doOnClickAdd: (item: MusicListModel.MusicModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list: List<MusicListModel.MusicModel> = listOf()
    fun setItemList(list: List<MusicListModel.MusicModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_music, viewGroup, false)

        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemHolder) {
            val item = list[position]

            holder.apply {

                try {
                    Glide.with(context).asDrawable().clone()
                        .load(item.snippet.thumbnails.default.url)
                        .centerCrop()
                        .dontAnimate()
                        .into(iv_thumbnail)
                } catch (e: Exception) {
                    Timber.e(e)
                }

                tv_title.text = item.snippet.title
                tv_artist.text = item.snippet.description

                iv_add.setOnClickListener {
                    doOnClickAdd(item)
                }
            }
        }
    }

    inner class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}