package com.network.base.utility

import com.network.base.network.NetworkBoundResource
import javax.inject.Singleton

@Singleton
class Query {
    var params: List<Any> = listOf()
    var boundType: Int = 0

    companion object{
        fun query(params: List<Any>) =
                Query().apply {
                    this.params = params
                    boundType = NetworkBoundResource.BOUND_FROM_BACKEND
                }
    }
}