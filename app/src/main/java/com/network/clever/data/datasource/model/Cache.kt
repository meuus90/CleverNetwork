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

package com.network.clever.data.datasource.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.network.clever.data.datasource.dao.item.PlaylistDao
import com.network.clever.data.datasource.model.item.PlaylistModel

/**
 * Main cache description.
 */
@Database(
    entities = [
        PlaylistModel::class
    ], exportSchema = false, version = 1
)
//@TypeConverters(BigDecimalTypeConverter::class, StringListTypeConverter::class)
abstract class Cache : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}