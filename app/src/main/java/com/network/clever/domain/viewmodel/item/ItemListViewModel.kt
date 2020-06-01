package com.network.clever.domain.viewmodel.item

import androidx.lifecycle.viewModelScope
import com.network.clever.domain.viewmodel.BaseViewModel
import com.network.base.network.Resource
import com.network.base.utility.Parameters
import com.network.base.utility.SingleLiveEvent
import com.network.clever.domain.usecase.item.ItemListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemListViewModel
@Inject
constructor(private val useCase: ItemListUseCase) : BaseViewModel<Parameters, Int>() {
    internal var assets = SingleLiveEvent<Resource>()

    override fun pullTrigger(params: Parameters) {
        viewModelScope.launch {
            assets = useCase.execute(viewModelScope, params)
        }
    }
}