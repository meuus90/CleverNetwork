package com.network.clever.utility

import com.network.clever.data.datasource.model.item.MusicListModel

object CollectionExt {
    fun sortList(list: ArrayList<MusicListModel.MusicModel>): ArrayList<MusicListModel.MusicModel> {
        val newList = arrayListOf<MusicListModel.MusicModel>()
        for (pos in list.indices) {
            newList.add(list[pos])
            newList[pos].orderId = pos
        }

        return newList
    }
}