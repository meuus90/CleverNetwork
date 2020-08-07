package com.network.clever.domain.viewmodel.item

import androidx.lifecycle.viewModelScope
import com.meuus.base.network.Resource
import com.meuus.base.utility.Params
import com.meuus.base.utility.SingleLiveEvent
import com.network.clever.domain.usecase.item.PlaylistUseCase
import com.network.clever.domain.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistViewModel
@Inject
constructor(private val useCase: PlaylistUseCase) : BaseViewModel<Params, Int>() {
    internal var playlist = SingleLiveEvent<Resource>()

    override fun pullTrigger(params: Params) {
        viewModelScope.launch {
            playlist = useCase.execute(viewModelScope, params)
        }
    }
}