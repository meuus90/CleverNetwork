package com.network.clever.presentation.home.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.meuus.base.view.BaseViewHolder
import com.network.clever.R
import com.network.clever.data.datasource.model.item.PlaylistListModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_playlist.*
import timber.log.Timber

class PlaylistListAdapter(val doOnClick: (item: PlaylistListModel.PlaylistModel) -> Unit) :
    PagedListAdapter<PlaylistListModel.PlaylistModel, BaseViewHolder<PlaylistListModel.PlaylistModel>>(
        DIFF_CALLBACK
    ) {
    companion object {
        private val PAYLOAD_TITLE = Any()

        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<PlaylistListModel.PlaylistModel>() {
                override fun areItemsTheSame(
                    oldItem: PlaylistListModel.PlaylistModel,
                    newItem: PlaylistListModel.PlaylistModel
                ): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: PlaylistListModel.PlaylistModel,
                    newItem: PlaylistListModel.PlaylistModel
                ): Boolean =
                    oldItem == newItem

                override fun getChangePayload(
                    oldItem: PlaylistListModel.PlaylistModel,
                    newItem: PlaylistListModel.PlaylistModel
                ): Any? {
                    return if (sameExceptTitle(oldItem, newItem)) {
                        PAYLOAD_TITLE
                    } else {
                        null
                    }
                }
            }

        private fun sameExceptTitle(
            oldItem: PlaylistListModel.PlaylistModel,
            newItem: PlaylistListModel.PlaylistModel
        ): Boolean {
            return oldItem.copy(id = newItem.id) == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<PlaylistListModel.PlaylistModel> {
        val inflater = LayoutInflater.from(parent.context.applicationContext)
        val view = inflater.inflate(R.layout.item_playlist, parent, false)
        return PlayItemHolder(view, this)
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<PlaylistListModel.PlaylistModel>,
        position: Int
    ) {
        val item = getItem(position)

        item?.let {
            if (holder is PlayItemHolder) {
                holder.bindItemHolder(holder, it, position)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id.hashCode().toLong()
    }

    private var searchedText = ""
    fun searchText(text: String) {
        searchedText = text
        notifyDataSetChanged()
    }

    class PlayItemHolder(
        override val containerView: View,
        private val adapter: PlaylistListAdapter
    ) : BaseViewHolder<PlaylistListModel.PlaylistModel>(containerView), LayoutContainer {
        @SuppressLint("SetTextI18n")
        override fun bindItemHolder(
            holder: BaseViewHolder<PlaylistListModel.PlaylistModel>,
            item: PlaylistListModel.PlaylistModel,
            position: Int
        ) {
            try {
                Glide.with(context).asDrawable().clone()
                    .load(item.imageUrl)
                    .centerCrop()
                    .dontAnimate()
                    .into(iv_thumbnail)
            } catch (e: Exception) {
                Timber.e(e)
            }

            tv_name.text = item.name

            v_root.setOnClickListener {
                adapter.doOnClick(item)
            }
        }

        override fun onItemSelected() {
            containerView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            containerView.setBackgroundColor(0)
        }
    }
}