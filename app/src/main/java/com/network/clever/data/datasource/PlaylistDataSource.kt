package com.network.clever.data.datasource

import androidx.paging.PageKeyedDataSource
import com.network.clever.data.datasource.model.item.PlaylistListModel

class PlaylistDataSource(private val List: MutableList<PlaylistListModel.PlaylistModel>) :
    PageKeyedDataSource<Int, PlaylistListModel.PlaylistModel>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PlaylistListModel.PlaylistModel>
    ) {
        callback.onResult(List, null, null)
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PlaylistListModel.PlaylistModel>
    ) {
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PlaylistListModel.PlaylistModel>
    ) {
    }
}