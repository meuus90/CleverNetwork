package com.network.clever.domain.viewmodel.item

import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.meuus.base.utility.Params
import com.meuus.base.utility.SingleLiveEvent
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.domain.usecase.item.MyPlaylistPagedUseCase
import com.network.clever.domain.usecase.item.MyPlaylistUseCase
import com.network.clever.domain.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPlaylistViewModel
@Inject
constructor(
    private val useCase: MyPlaylistPagedUseCase,
    private val mutableUseCase: MyPlaylistUseCase
) :
    BaseViewModel<Params, Int>() {
    internal var playlist = SingleLiveEvent<PagedList<MusicListModel.MusicModel>>()
    internal var playlistAll = SingleLiveEvent<MutableList<MusicListModel.MusicModel>>()

    override fun pullTrigger(params: Params) {
        viewModelScope.launch {
            playlist = useCase.execute(viewModelScope, params)
        }
    }

    fun getMutableList(params: Params) {
        viewModelScope.launch {
            playlistAll = mutableUseCase.execute(viewModelScope, params)
        }
    }
}