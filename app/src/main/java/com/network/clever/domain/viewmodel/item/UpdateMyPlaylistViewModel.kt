package com.network.clever.domain.viewmodel.item

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.meuus.base.utility.Params
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.domain.usecase.item.UpdateMyPlaylistUseCase
import com.network.clever.domain.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateMyPlaylistViewModel
@Inject
constructor(private val useCase: UpdateMyPlaylistUseCase) : BaseViewModel<Params, Int>() {
    internal var playlist = MutableLiveData<ArrayList<MusicListModel.MusicModel>>()

    val org = MutableLiveData<Params>()

    override fun pullTrigger(params: Params) {
//        var job: Deferred<Unit>? = null
//        viewModelScope.launch {
//            org.asFlow()
//                .debounce(500)
//                .distinctUntilChanged()
//                .collect {
//                    job?.cancel()
//                    job = async {
//                        playlist.value = useCase.execute(viewModelScope, params).value
//                    }
//                }
//        }
        viewModelScope.launch {
            org.value = params
            org.asFlow()
                .debounce(500)
                .distinctUntilChanged()
                .collect {
                    playlist.value = useCase.execute(viewModelScope, it).value
                }
        }
    }
}