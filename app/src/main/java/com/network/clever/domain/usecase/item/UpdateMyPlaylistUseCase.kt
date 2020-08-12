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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meuus.base.utility.Params
import com.network.clever.data.datasource.dao.item.MusicDao
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.domain.usecase.BaseUseCase
import com.network.clever.utility.CollectionExt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateMyPlaylistUseCase
@Inject
constructor(private val dao: MusicDao) :
    BaseUseCase<Params, ArrayList<MusicListModel.MusicModel>>() {
    companion object {
        const val UPDATE_ALL = 0
        const val ADD_ALL = 1
        const val ADD_ITEM = 2
        const val DELETE_ALL = 3
        const val DELETE_ITEM = 4
    }

    private val liveData by lazy { MutableLiveData<ArrayList<MusicListModel.MusicModel>>() }

    override suspend fun execute(
        viewModelScope: CoroutineScope,
        params: Params
    ): LiveData<ArrayList<MusicListModel.MusicModel>> {
        val state = params.query.params[0] as Int
        val result =
            when (state) {
                UPDATE_ALL -> {
                    dao.clear()

                    val music = params.query.params[1] as ArrayList<MusicListModel.MusicModel>

                    val list = CollectionExt.sortList(music)
                    GlobalScope.async { dao.insert(list) }.await()
                    list
                }
                ADD_ALL -> {
                    val music = params.query.params[1] as ArrayList<MusicListModel.MusicModel>

                    val old = GlobalScope.async { dao.getPlaylists() }.await()
                    music.addAll(old)

                    val list = CollectionExt.sortList(music)

                    GlobalScope.async { dao.insert(list) }.await()
                    list
                }
                ADD_ITEM -> {
                    val music = params.query.params[1] as MusicListModel.MusicModel

                    val old = GlobalScope.async { dao.getPlaylists() }.await()
                    val new = arrayListOf(music)
                    new.addAll(old)

                    val list = CollectionExt.sortList(new)

                    GlobalScope.async { dao.insert(list) }.await()
                    list
                }
                DELETE_ALL -> {
                    dao.clear()
                    arrayListOf()
                }
                DELETE_ITEM -> {
                    val old = GlobalScope.async { dao.getPlaylists() }.await()
                    val music = params.query.params[1] as MusicListModel.MusicModel

                    val new = arrayListOf<MusicListModel.MusicModel>()
                    new.addAll(old)

                    new.removeIf {
                        it.snippet.resourceId.videoId == music.snippet.resourceId.videoId
                    }

                    val list = CollectionExt.sortList(new)

                    GlobalScope.async { dao.insert(list) }.await()
                    list
                }
                else -> {
                    arrayListOf()
                }
            }
        liveData.value = result

        return liveData
    }
}