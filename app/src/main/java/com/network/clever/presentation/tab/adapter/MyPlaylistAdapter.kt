package com.network.clever.presentation.tab.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.network.clever.R
import com.network.clever.data.datasource.model.item.MusicListModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_my_music.*
import timber.log.Timber
import java.util.*

class MyPlaylistAdapter(
    val context: Context,
    val onStartMusic: (list: ArrayList<MusicListModel.MusicModel>, videoId: String) -> Unit,
    val onDataSetChanged: (list: ArrayList<MusicListModel.MusicModel>) -> Unit,
    private val listener: ItemDragListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemActionListener {
    var list: ArrayList<MusicListModel.MusicModel> = arrayListOf()
    fun setItemList(list: ArrayList<MusicListModel.MusicModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_my_music, viewGroup, false)

        return ItemHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("DefaultLocale", "ClickableViewAccessibility")
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
                tv_artist.text = item.snippet.channelTitle

                v_root.setOnClickListener {
                    onStartMusic(list, item.snippet.resourceId.videoId)
                }

                iv_drag_drop.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        listener.onStartDrag(this)
                    }
                    false
                }
            }
        }
    }

    inner class ItemHolder(override val containerView: View, val listener: ItemDragListener) :
        RecyclerView.ViewHolder(containerView), LayoutContainer

    override fun onItemMoved(from: Int, to: Int) {
        if (from == to) {
            return
        }

        val fromItem = list.removeAt(from)
        list.add(to, fromItem)
        notifyItemMoved(from, to)
    }

    override fun onItemSwiped(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }
}