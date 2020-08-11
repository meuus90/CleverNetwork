package com.network.clever.presentation.tab.adapter

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
import com.network.clever.data.datasource.model.item.MusicListModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_my_music.*
import timber.log.Timber

class ContentsAdapter(
    val doOnClick: (item: ArrayList<MusicListModel.MusicModel>) -> Unit,
    val doOnClickDelete: (item: MusicListModel.MusicModel) -> Unit
) : PagedListAdapter<MusicListModel.MusicModel, BaseViewHolder<MusicListModel.MusicModel>>(
    DIFF_CALLBACK
) {
    companion object {
        private val PAYLOAD_TITLE = Any()

        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<MusicListModel.MusicModel>() {
                override fun areItemsTheSame(
                    oldItem: MusicListModel.MusicModel,
                    newItem: MusicListModel.MusicModel
                ): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: MusicListModel.MusicModel,
                    newItem: MusicListModel.MusicModel
                ): Boolean =
                    oldItem == newItem

                override fun getChangePayload(
                    oldItem: MusicListModel.MusicModel,
                    newItem: MusicListModel.MusicModel
                ): Any? {
                    return if (sameExceptTitle(
                            oldItem,
                            newItem
                        )
                    ) {
                        PAYLOAD_TITLE
                    } else {
                        null
                    }
                }
            }

        private fun sameExceptTitle(
            oldItem: MusicListModel.MusicModel,
            newItem: MusicListModel.MusicModel
        ): Boolean {
            return oldItem.copy(id = newItem.id) == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<MusicListModel.MusicModel> {
        val inflater = LayoutInflater.from(parent.context.applicationContext)
        val view = inflater.inflate(R.layout.item_my_music, parent, false)
        return PlayItemHolder(
            view,
            this
        )
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<MusicListModel.MusicModel>,
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
        private val adapter: ContentsAdapter
    ) : BaseViewHolder<MusicListModel.MusicModel>(containerView), LayoutContainer {
        @SuppressLint("SetTextI18n")
        override fun bindItemHolder(
            holder: BaseViewHolder<MusicListModel.MusicModel>,
            item: MusicListModel.MusicModel,
            position: Int
        ) {
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
                adapter.currentList?.toMutableList()?.let {
                    adapter.doOnClick(ArrayList(it))
                }
            }

            iv_delete.setOnClickListener {
                adapter.doOnClickDelete(item)
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