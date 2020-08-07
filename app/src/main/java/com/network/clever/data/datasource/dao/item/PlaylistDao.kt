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

package com.network.clever.data.datasource.dao.item

import androidx.room.Dao
import androidx.room.Query
import com.network.clever.data.datasource.dao.BaseDao
import com.network.clever.data.datasource.model.item.PlaylistModel

@Dao
interface PlaylistDao : BaseDao<PlaylistModel> {
    @Query("SELECT * FROM Playlist")
    fun getPlaylists(): MutableList<PlaylistModel>

    @Query("DELETE FROM Playlist")
    suspend fun clear()
}