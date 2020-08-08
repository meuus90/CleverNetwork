package com.network.clever.presentation.stream

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
import com.meuus.base.utility.Query
import com.meuus.base.view.AutoClearedValue
import com.network.clever.R
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.domain.viewmodel.item.MusicViewModel
import com.network.clever.presentation.BaseFragment
import com.network.clever.presentation.stream.adapter.ContentsAdapter
import kotlinx.android.synthetic.main.fragment_playlist.*
import javax.inject.Inject

class PlaylistFragment : BaseFragment() {
    companion object {
        fun newInstance() = PlaylistFragment().apply {
            arguments = Bundle(1).apply {
                putString(FRAGMENT_TAG, PlaylistFragment::class.java.name)
            }
        }
    }

    @Inject
    internal lateinit var musicViewModel: MusicViewModel

    private lateinit var adapter: ContentsAdapter

    private val playlistActivity: PlaylistActivity by lazy {
        activity as PlaylistActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val acvView =
            AutoClearedValue(
                this,
                inflater.inflate(R.layout.fragment_playlist, container, false)
            )
        return acvView.get()?.rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = ContentsAdapter { item ->

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

        val query = Query.query(listOf(playlistActivity.playlist.key))

        musicViewModel.pullTrigger(Params(query))
        musicViewModel.music.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.getStatus()) {
                Status.SUCCESS -> {
                    val list = resource.getData() as PagedList<MusicListModel.MusicModel>
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