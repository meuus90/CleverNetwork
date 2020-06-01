package com.network.clever.data.datasource

import androidx.paging.PageKeyedDataSource
import com.network.clever.data.datasource.model.item.Item

class ItemListDataSource(private val List: List<Item>) :
    PageKeyedDataSource<Int, Item>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Item>
    ) {
        callback.onResult(List as MutableList<Item>, null, null)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {
    }
}