package com.network.clever.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.network.base.view.AutoClearedValue
import com.network.clever.R
import com.network.clever.presentation.BaseFragment
import com.network.clever.presentation.MainActivity

class HomeFragment : BaseFragment() {
    private val mainActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val acvView =
            AutoClearedValue(
                this,
                inflater.inflate(R.layout.fragment_home, container, false)
            )
        return acvView.get()?.rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

//    private fun getList() {
//        val query = doQuery(listOf("football"))
//        query.boundType = BOUND_FROM_BACKEND
//
//        itemListViewModel
//
//        itemListViewModel.pullTrigger(Parameters(query))
//        itemListViewModel.assets.observe(this.viewLifecycleOwner, Observer { resource ->
//            when (resource.getStatus()) {
//                Status.SUCCESS -> {
//                    resource.getData()?.let { list ->
//                        @Suppress("UNCHECKED_CAST")
//                        list as PagedList<Item>
//                        acvAdapter.get()?.submitList(list)
//                    }
//                }
//
//                Status.LOADING -> {
//                }
//
//                Status.ERROR -> {
//                }
//
//                else -> {
//                }
//            }
//
//            if (resource.getStatus() != Status.LOADING) {
//            }
//        })
//    }
}