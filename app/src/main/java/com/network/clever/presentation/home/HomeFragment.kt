package com.network.clever.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.meuus.base.network.Status
import com.meuus.base.utility.Params
import com.meuus.base.utility.Query.Companion.query
import com.meuus.base.view.AutoClearedValue
import com.network.clever.R
import com.network.clever.data.datasource.model.item.PlaylistModel
import com.network.clever.domain.viewmodel.item.PlaylistViewModel
import com.network.clever.presentation.BaseFragment
import com.network.clever.presentation.MainActivity
import com.network.clever.presentation.home.adapter.PlaylistAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : BaseFragment() {
    companion object {
        fun newInstance() = HomeFragment().apply {
            arguments = Bundle(1).apply {
                putString(FRAGMENT_TAG, HomeFragment::class.java.name)
            }
        }
    }

    @Inject
    internal lateinit var playlistViewModel: PlaylistViewModel

    private lateinit var adapter: PlaylistAdapter

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


        adapter = PlaylistAdapter { item ->

        }
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(false)
        recyclerView.setItemViewCacheSize(10)
        recyclerView.itemAnimator?.changeDuration = 0
        (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        recyclerView.isVerticalScrollBarEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        swipeRefreshLayout.setOnRefreshListener {
            getPlaylist()
        }
        getPlaylist()
    }

    private fun getPlaylist() {
        setLoading(true)

        val query = query(listOf())

        playlistViewModel.pullTrigger(Params(query))
        playlistViewModel.playlist.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.getStatus()) {
                Status.SUCCESS -> {
                    val list = resource.getData() as PagedList<PlaylistModel>
                    adapter.submitList(list)

                    setLoading(false)
                }

                Status.LOADING -> {
                }

                Status.ERROR -> {
                    setLoading(false)
                }

                else -> {
                }
            }
        })
    }

    private fun setLoading(show: Boolean) {
        if (show) {
            if (!swipeRefreshLayout.isRefreshing)
                showLoading(true)
        } else {
            swipeRefreshLayout.isRefreshing = false
            showLoading(false)
        }
    }
}