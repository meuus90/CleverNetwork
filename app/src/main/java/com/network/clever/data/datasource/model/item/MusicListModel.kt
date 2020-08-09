package com.network.clever.data.datasource.model.item

import android.os.Parcelable
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.network.clever.data.datasource.model.BaseData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MusicListModel(
    val items: MutableList<MusicModel>,
    val nextPageToken: String
) : BaseData(), Parcelable {

    @Parcelize
    @Entity(tableName = "Music")
    @TypeConverters(
        ResourceIdTypeConverter::class,
        ThumbnailsTypeConverter::class,
        ThumbnailTypeConverter::class
    )
    data class MusicModel(
        @field:PrimaryKey
        @field:ColumnInfo(name = "id") val id: String,
//        @field:ColumnInfo(name = "playlistId") val playlistId: String,

        @field:Embedded(prefix = "snippet") val snippet: Snippet
    ) : BaseData(), Parcelable

    @Parcelize
    data class Snippet(
        @field:ColumnInfo(name = "publishedAt") val publishedAt: String,
        @field:ColumnInfo(name = "title") val title: String,
        @field:ColumnInfo(name = "description") val description: String,
        @field:ColumnInfo(name = "channelId") val channelId: String,
        @field:ColumnInfo(name = "playlistId") val playlistId: String,

        @field:ColumnInfo(name = "resourceId") val resourceId: ResourceId,
        @field:ColumnInfo(name = "thumbnails") val thumbnails: Thumbnails
    ) : BaseData(), Parcelable {
        constructor() : this("", "", "", "", "", ResourceId(), Thumbnails())
    }

    @Parcelize
    data class ResourceId(
        @field:ColumnInfo(name = "kind") val kind: String,
        @field:ColumnInfo(name = "videoId") val videoId: String
    ) : BaseData(), Parcelable {
        constructor() : this("", "")
    }

    @Parcelize
    data class Thumbnails(
        @field:ColumnInfo(name = "default") val default: Thumbnail
    ) : BaseData(), Parcelable {
        constructor() : this(Thumbnail())
    }

    @Parcelize
    data class Thumbnail(
        @field:ColumnInfo(name = "url") val url: String
    ) : BaseData(), Parcelable {
        constructor() : this("")
    }

    class ResourceIdTypeConverter {
        @TypeConverter
        fun itemToString(value: ResourceId?): String {
            return Gson().toJson(value)
        }

        @TypeConverter
        fun stringToItem(value: String?): ResourceId {
            val item = object : TypeToken<ResourceId>() {}.type
            return Gson().fromJson(value, item)
        }
    }

    class ThumbnailsTypeConverter {
        @TypeConverter
        fun itemToString(value: Thumbnails?): String {
            return Gson().toJson(value)
        }

        @TypeConverter
        fun stringToItem(value: String?): Thumbnails {
            val item = object : TypeToken<Thumbnails>() {}.type
            return Gson().fromJson(value, item)
        }
    }

    class ThumbnailTypeConverter {
        @TypeConverter
        fun itemToString(value: Thumbnail?): String {
            return Gson().toJson(value)
        }

        @TypeConverter
        fun stringToItem(value: String?): Thumbnail {
            val item = object : TypeToken<Thumbnail>() {}.type
            return Gson().fromJson(value, item)
        }
    }
}