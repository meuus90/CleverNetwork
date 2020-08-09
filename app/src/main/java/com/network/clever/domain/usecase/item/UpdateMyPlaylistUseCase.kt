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
import com.meuus.base.utility.Params
import com.meuus.base.utility.Query
import com.meuus.base.utility.SingleLiveEvent
import com.network.clever.data.datasource.dao.item.MusicDao
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.domain.usecase.BaseUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateMyPlaylistUseCase
@Inject
constructor(private val dao: MusicDao) : BaseUseCase<Params, Boolean>() {
    companion object {
        const val UPDATE_ALL = 0
        const val ADD_ALL = 1
        const val ADD_ITEM = 2
    }

    private val liveData by lazy { MutableLiveData<Query>() }

    override suspend fun execute(
        viewModelScope: CoroutineScope,
        params: Params
    ): SingleLiveEvent<Boolean> {
        setQuery(params)

        val state = params.query.params[0] as Int
        val result = when (state) {
            UPDATE_ALL -> {
                val music = params.query.params[1] as MutableList<MusicListModel.MusicModel>

                dao.insert(music)
                true
            }
            ADD_ALL -> {
                val music = params.query.params[1] as MutableList<MusicListModel.MusicModel>

                dao.insert(music)

                true
            }
            ADD_ITEM -> {
                val music = params.query.params[1] as MusicListModel.MusicModel

                dao.clear()
                dao.insert(music)
                true
            }
            else -> {
                false
            }
        }

        val resultEvent = SingleLiveEvent<Boolean>()
        resultEvent.addSource(liveData) {
            resultEvent.value = result
        }

        return resultEvent
    }

    private fun setQuery(params: Params) {
        liveData.value = params.query
    }
}