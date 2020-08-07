package com.network.clever.data.datasource

import androidx.paging.PageKeyedDataSource
import com.network.clever.data.datasource.model.item.ItemModel

class ItemListDataSource(private val List: List<ItemModel>) :
    PageKeyedDataSource<Int, ItemModel>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ItemModel>
    ) {
        callback.onResult(List as MutableList<ItemModel>, null, null)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ItemModel>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ItemModel>) {
    }
}