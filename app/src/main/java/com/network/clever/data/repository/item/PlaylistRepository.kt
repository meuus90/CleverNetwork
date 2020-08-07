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
import com.network.clever.data.datasource.ItemListDataSource
import com.network.clever.data.datasource.dao.item.PlaylistDao
import com.network.clever.data.datasource.model.item.ContentsModel
import com.network.clever.data.datasource.model.item.PlaylistModel
import com.network.clever.data.repository.BaseRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistRepository
@Inject
constructor(private val dao: PlaylistDao) : BaseRepository<Query>() {
    override suspend fun work(liveData: MutableLiveData<Query>): SingleLiveEvent<Resource> {
        return object :
            NetworkBoundResource<PagedList<PlaylistModel>, ContentsModel>(liveData.value?.boundType!!) {
            override suspend fun workToCache(item: ContentsModel) {
                clearCache()
                dao.insert(item.playlists)
            }

            override suspend fun loadFromCache(
                isLatest: Boolean,
                itemCount: Int,
                pages: Int
            ): LiveData<PagedList<PlaylistModel>> {
                val config = PagedList.Config.Builder()
                    .setInitialLoadSizeHint(20)
                    .setPageSize(itemCount)
                    .setPrefetchDistance(10)
                    .setEnablePlaceholders(true)
                    .build()

                return LivePagedListBuilder(object :
                    DataSource.Factory<Int, PlaylistModel>() {
                    override fun create(): DataSource<Int, PlaylistModel> {

                        val list = dao.getPlaylists()
                        return ItemListDataSource(list)
                    }
                }, /* PageList Config */ config).build()
            }

            override suspend fun doNetworkJob() =
                firebaseAPI.getPlaylists()

            override fun onNetworkError(errorMessage: String?, errorCode: Int) =
                Timber.e("Network-Error: $errorMessage")

            override fun onFetchFailed(failedMessage: String?) =
                Timber.e("Fetch-Failed: $failedMessage")
        }.getAsSingleLiveEvent()
    }
}