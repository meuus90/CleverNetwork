package com.network.clever.data.datasource

import androidx.paging.PageKeyedDataSource
import com.network.clever.data.datasource.model.item.MusicListModel

class MusicDataSource(private val List: MutableList<MusicListModel.MusicModel>) :
    PageKeyedDataSource<Int, MusicListModel.MusicModel>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MusicListModel.MusicModel>
    ) {
        callback.onResult(List, null, null)
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MusicListModel.MusicModel>
    ) {
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MusicListModel.MusicModel>
    ) {
    }
}