package com.network.clever.data.datasource.model.item

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.network.clever.data.datasource.model.BaseData
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "Playlist")
data class PlaylistModel(
    @field:PrimaryKey
    @field:ColumnInfo(name = "id") val id: Int,
    @field:ColumnInfo(name = "name") val name: String,
    @field:ColumnInfo(name = "key") val key: String,
    @field:ColumnInfo(name = "imageUrl") val imageUrl: String
) : BaseData(), Parcelable