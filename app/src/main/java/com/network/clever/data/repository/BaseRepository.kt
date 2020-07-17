package com.network.clever.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meuus.base.network.Resource
import com.network.clever.data.datasource.network.ServerAPI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
abstract class BaseRepository<T> {
    @field:Inject
    lateinit var serverAPI: ServerAPI

    abstract suspend fun work(liveData: MutableLiveData<T>): LiveData<Resource>

    companion object {
        internal const val PER_PAGE = 20
        internal const val INDEX = 1
    }
}