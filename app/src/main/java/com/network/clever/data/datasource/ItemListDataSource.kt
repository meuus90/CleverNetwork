package com.network.clever.data.datasource

import androidx.paging.PageKeyedDataSource
import com.network.clever.data.datasource.model.item.PlaylistModel

class ItemListDataSource(private val List: MutableList<PlaylistModel>) :
    PageKeyedDataSource<Int, PlaylistModel>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PlaylistModel>
    ) {
        callback.onResult(List, null, null)
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PlaylistModel>
    ) {
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PlaylistModel>
    ) {
    }
}