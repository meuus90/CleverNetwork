package com.network.clever.data.datasource.model.item

import android.os.Parcelable
import com.network.clever.data.datasource.model.BaseData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContentsModel(
    val playlists: MutableList<PlaylistModel>
) : BaseData(), Parcelable