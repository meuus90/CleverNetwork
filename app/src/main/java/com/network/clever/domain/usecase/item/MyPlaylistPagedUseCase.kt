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
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.meuus.base.utility.Params
import com.meuus.base.utility.Query
import com.meuus.base.utility.SingleLiveEvent
import com.network.clever.data.datasource.MusicDataSource
import com.network.clever.data.datasource.dao.item.MusicDao
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.domain.usecase.BaseUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPlaylistPagedUseCase
@Inject
constructor(private val dao: MusicDao) :
    BaseUseCase<Params, PagedList<MusicListModel.MusicModel>>() {
    private val liveData by lazy { MutableLiveData<Query>() }

    override suspend fun execute(
        viewModelScope: CoroutineScope,
        params: Params
    ): SingleLiveEvent<PagedList<MusicListModel.MusicModel>> {
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(20)
            .setPageSize(10)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(true)
            .build()

        val liveData = LivePagedListBuilder(object :
            DataSource.Factory<Int, MusicListModel.MusicModel>() {
            override fun create(): DataSource<Int, MusicListModel.MusicModel> {

                val list = dao.getPlaylists()
                return MusicDataSource(list)
            }
        }, /* PageList Config */ config).build()


        val resultEvent = SingleLiveEvent<PagedList<MusicListModel.MusicModel>>()
        resultEvent.addSource(liveData) {
            resultEvent.value = it
        }

        return resultEvent
    }

    private fun setQuery(params: Params) {
        liveData.value = params.query
    }
}