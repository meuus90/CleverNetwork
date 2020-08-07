package com.network.clever.presentation.home.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.meuus.base.view.BaseViewHolder
import com.network.clever.R
import com.network.clever.data.datasource.model.item.ItemModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_play.*

class ItemListAdapter(val doOnClick: (item: ItemModel) -> Unit) :
    PagedListAdapter<ItemModel, BaseViewHolder<ItemModel>>(
        DIFF_CALLBACK
    ) {
    companion object {
        private val PAYLOAD_TITLE = Any()

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemModel>() {
            override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean =
                oldItem == newItem

            override fun getChangePayload(oldItem: ItemModel, newItem: ItemModel): Any? {
                return if (sameExceptTitle(oldItem, newItem)) {
                    PAYLOAD_TITLE
                } else {
                    null
                }
            }
        }

        private fun sameExceptTitle(oldItem: ItemModel, newItem: ItemModel): Boolean {
            return oldItem.copy(id = newItem.id) == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ItemModel> {
        val inflater = LayoutInflater.from(parent.context.applicationContext)
        val view = inflater.inflate(R.layout.item_play, parent, false)
        return PlayItemHolder(view, this)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ItemModel>, position: Int) {
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
        private val adapter: ItemListAdapter
    ) : BaseViewHolder<ItemModel>(containerView), LayoutContainer {
        @SuppressLint("SetTextI18n")
        override fun bindItemHolder(holder: BaseViewHolder<ItemModel>, item: ItemModel, position: Int) {
            tv_name.text = item.snippet.title
        }

        override fun onItemSelected() {
            containerView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            containerView.setBackgroundColor(0)
        }
    }
}