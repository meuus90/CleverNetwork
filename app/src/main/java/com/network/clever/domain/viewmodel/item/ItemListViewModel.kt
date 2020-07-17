package com.network.clever.domain.viewmodel.item

import androidx.lifecycle.viewModelScope
import com.meuus.base.network.Resource
import com.meuus.base.utility.Params
import com.meuus.base.utility.SingleLiveEvent
import com.network.clever.domain.viewmodel.BaseViewModel
import com.network.clever.domain.usecase.item.ItemListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemListViewModel
@Inject
constructor(private val useCase: ItemListUseCase) : BaseViewModel<Params, Int>() {
    internal var assets = SingleLiveEvent<Resource>()

    override fun pullTrigger(params: Params) {
        viewModelScope.launch {
            assets = useCase.execute(viewModelScope, params)
        }
    }
}