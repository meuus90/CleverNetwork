package com.network.clever.presentation.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.meuus.base.network.Status
import com.meuus.base.utility.Params
import com.meuus.base.utility.Query
import com.meuus.base.view.AutoClearedValue
import com.network.clever.R
import com.network.clever.data.datasource.model.item.MusicListModel
import com.network.clever.domain.usecase.item.UpdateMyPlaylistUseCase.Companion.ADD_ALL
import com.network.clever.domain.usecase.item.UpdateMyPlaylistUseCase.Companion.ADD_ITEM
import com.network.clever.domain.usecase.item.UpdateMyPlaylistUseCase.Companion.UPDATE_ALL
import com.network.clever.domain.viewmodel.item.MusicViewModel
import com.network.clever.domain.viewmodel.item.UpdateMyPlaylistViewModel
import com.network.clever.presentation.BaseFragment
import com.network.clever.presentation.Caller
import com.network.clever.presentation.playlist.adapter.PlaylistAdapter
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

    @Inject
    internal lateinit var updateMyPlaylistViewModel: UpdateMyPlaylistViewModel

    private lateinit var adapter: PlaylistAdapter

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

        adapter =
            PlaylistAdapter(context) { item ->
                val query = Query.query(listOf(ADD_ITEM, item))
                update(query)
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

        iv_add.setOnClickListener {
            val query = Query.query(listOf(ADD_ALL, list.items))
            update(query)
        }

        iv_play.setOnClickListener {
            val query = Query.query(listOf(UPDATE_ALL, list.items))
            update(query)
        }
    }

    private fun update(query: Query) {
        updateMyPlaylistViewModel.pullTrigger(Params(query))
        updateMyPlaylistViewModel.playlist.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess) {
                Caller.openMyPlaylist(playlistActivity)
            } else {

            }
        })
    }

    lateinit var list: MusicListModel
    private fun getPlaylist() {
        setLoading(true)

        val key = playlistActivity.platlist.key
        val query = Query.query(listOf(key))

        musicViewModel.pullTrigger(Params(query))
        musicViewModel.music.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.getStatus()) {
                Status.SUCCESS -> {
                    list = resource.getData() as MusicListModel
                    adapter.setItemList(list.items)

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