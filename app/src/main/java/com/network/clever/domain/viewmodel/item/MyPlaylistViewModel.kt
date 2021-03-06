package com.network.clever.domain.viewmodel.item

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.meuus.base.utility.Params
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.domain.usecase.item.MyPlaylistUseCase
import com.network.clever.domain.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPlaylistViewModel
@Inject
constructor(private val useCase: MyPlaylistUseCase) :
    BaseViewModel<Params, Int>() {
    internal var playlist = MutableLiveData<ArrayList<MusicListModel.MusicModel>>()

    override fun pullTrigger(params: Params) {
        viewModelScope.launch {
            playlist.value = useCase.execute(viewModelScope, params).value
        }
    }
}