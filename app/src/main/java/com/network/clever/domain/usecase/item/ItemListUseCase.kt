/*
 * Copyright (C)  2020 MeUuS90
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.network.clever.domain.usecase.item

import androidx.lifecycle.MutableLiveData
import com.network.clever.domain.usecase.BaseUseCase
import com.network.base.network.Resource
import com.network.base.utility.Params
import com.network.base.utility.Query
import com.network.base.utility.SingleLiveEvent
import com.network.clever.data.repository.item.ItemListRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemListUseCase
@Inject
constructor(private val repository: ItemListRepository) : BaseUseCase<Params, Resource>() {
    private val liveData by lazy { MutableLiveData<Query>() }

    override suspend fun execute(
        viewModelScope: CoroutineScope,
        params: Params
    ): SingleLiveEvent<Resource> {
        setQuery(params)

        return repository.work(this@ItemListUseCase.liveData)
    }

    private fun setQuery(params: Params) {
        liveData.value = params.query
    }
}