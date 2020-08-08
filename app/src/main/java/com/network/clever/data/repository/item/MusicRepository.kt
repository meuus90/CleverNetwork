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

package com.network.clever.data.repository.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.meuus.base.network.NetworkBoundResource
import com.meuus.base.network.Resource
import com.meuus.base.utility.Query
import com.meuus.base.utility.SingleLiveEvent
import com.network.clever.constant.AppConfig
import com.network.clever.data.datasource.MusicDataSource
import com.network.clever.data.datasource.dao.item.MusicDao
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.data.repository.BaseRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepository
@Inject
constructor(private val dao: MusicDao) : BaseRepository<Query>() {
    override suspend fun work(liveData: MutableLiveData<Query>): SingleLiveEvent<Resource> {
        return object :
            NetworkBoundResource<PagedList<MusicListModel.MusicModel>, MusicListModel>(liveData.value?.boundType!!) {
            override suspend fun workToCache(item: MusicListModel) {
                clearCache()
                dao.insert(item.items)
            }

            override suspend fun loadFromCache(
                isLatest: Boolean,
                itemCount: Int,
                pages: Int
            ): LiveData<PagedList<MusicListModel.MusicModel>> {
                val config = PagedList.Config.Builder()
                    .setInitialLoadSizeHint(20)
                    .setPageSize(itemCount)
                    .setPrefetchDistance(10)
                    .setEnablePlaceholders(true)
                    .build()

                return LivePagedListBuilder(object :
                    DataSource.Factory<Int, MusicListModel.MusicModel>() {
                    override fun create(): DataSource<Int, MusicListModel.MusicModel> {

                        val list = dao.getPlaylists()
                        return MusicDataSource(list)
                    }
                }, /* PageList Config */ config).build()
            }

            override suspend fun doNetworkJob() =
                youtubeAPI.getMusics(
                    liveData.value?.params?.get(0) as String,
                    "snippet",
                    10,
                    AppConfig.apiKey
                )

            override fun onNetworkError(errorMessage: String?, errorCode: Int) =
                Timber.e("Network-Error: $errorMessage")

            override fun onFetchFailed(failedMessage: String?) =
                Timber.e("Fetch-Failed: $failedMessage")
        }.getAsSingleLiveEvent()
    }
}