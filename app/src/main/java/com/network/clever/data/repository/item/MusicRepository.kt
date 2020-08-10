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

import androidx.lifecycle.MutableLiveData
import com.meuus.base.network.NetworkBoundResource
import com.meuus.base.network.Resource
import com.meuus.base.utility.Query
import com.meuus.base.utility.SingleLiveEvent
import com.network.clever.constant.AppConfig
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.data.repository.BaseRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepository
@Inject
constructor() : BaseRepository<Query>() {
    override suspend fun work(liveData: MutableLiveData<Query>): SingleLiveEvent<Resource> {
        return object :
            NetworkBoundResource<MusicListModel, MusicListModel>(liveData.value?.boundType!!) {
            override suspend fun doNetworkJob() =
                youtubeAPI.getMusics(
                    liveData.value?.params?.get(0) as String,
                    "snippet",
                    60,
                    AppConfig.apiKey
                )

            override fun onNetworkError(errorMessage: String?, errorCode: Int) =
                Timber.e("Network-Error: $errorMessage")

            override fun onFetchFailed(failedMessage: String?) =
                Timber.e("Fetch-Failed: $failedMessage")
        }.getAsSingleLiveEvent()
    }
}