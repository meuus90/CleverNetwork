package com.network.clever.presentation.tab

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.meuus.base.network.Status
import com.meuus.base.utility.Params
import com.meuus.base.utility.Query
import com.meuus.base.view.AutoClearedValue
import com.network.clever.R
import com.network.clever.data.datasource.model.item.PlaylistListModel
import com.network.clever.domain.viewmodel.item.PlaylistViewModel
import com.network.clever.presentation.BaseFragment
import com.network.clever.presentation.Caller
import com.network.clever.presentation.tab.adapter.PlaylistItemDecoration
import com.network.clever.presentation.tab.adapter.PlaylistListAdapter
import kotlinx.android.synthetic.main.fragment_playlist_list.*
import javax.inject.Inject
import kotlin.math.absoluteValue

class PlaylistListFragment : BaseFragment() {
    companion object {
        fun newInstance() = PlaylistListFragment().apply {
            arguments = Bundle(1).apply {
                putString(FRAGMENT_TAG, PlaylistListFragment::class.java.name)
            }
        }
    }

    @Inject
    internal lateinit var playlistViewModel: PlaylistViewModel

    private lateinit var adapter: PlaylistListAdapter
    private lateinit var itemDecoration: PlaylistItemDecoration
    private lateinit var gridManager: GridLayoutManager

    private val homeActivity: HomeActivity by lazy {
        activity as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val acvView =
            AutoClearedValue(
                this,
                inflater.inflate(R.layout.fragment_playlist_list, container, false)
            )
        return acvView.get()?.rootView
    }

    //    private var beforeSize = 0f
    private var spanCount = 3

    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = PlaylistListAdapter { item ->
            Caller.openPlaylist(homeActivity, item)
        }
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(false)
        recyclerView.setItemViewCacheSize(10)
        recyclerView.itemAnimator?.changeDuration = 0
        (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        recyclerView.isVerticalScrollBarEnabled = false
        gridManager = GridLayoutManager(context, spanCount)
        recyclerView.layoutManager = gridManager
        itemDecoration = PlaylistItemDecoration(context)
        recyclerView.addItemDecoration(itemDecoration)

//        recyclerView.setOnTouchListener { v, event ->
//            var pointerCount = event.pointerCount
//            if (pointerCount > 2) pointerCount = 2
//
//            when (event.action.and(MotionEvent.ACTION_MASK)) {
//                MotionEvent.ACTION_POINTER_DOWN -> {
//                    if (pointerCount > 1) {
//                        beforeSize = getSize(pointerCount, event)
//                    }
//                }
//                MotionEvent.ACTION_MOVE -> {
//                    if (pointerCount > 1) {
//                        val afterSize = getSize(pointerCount, event)
//
//                        if (beforeSize < afterSize - 500000) {
//                            if (spanCount > 1) {
//                                spanCount -= 1
//                                gridManager.spanCount = spanCount
//                                itemDecoration.setSpanCount(spanCount)
//                                adapter.notifyDataSetChanged()
//                                beforeSize = afterSize
//                            }
//
//                        } else if (beforeSize > afterSize + 500000) {
//                            if (spanCount < 5) {
//                                spanCount += 1
//                                gridManager.spanCount = spanCount
//                                itemDecoration.setSpanCount(spanCount)
//                                adapter.notifyDataSetChanged()
//                                beforeSize = afterSize
//                            }
//                        }
//                    } else {
//                        false
//                    }
//                }
//                MotionEvent.ACTION_UP -> {
//                    beforeSize = 0f
//                }
//            }
//
//
//            true
//        }

        swipeRefreshLayout.setOnRefreshListener {
            getPlaylist()
        }
        getPlaylist()
    }

    fun getSize(pointerCount: Int, event: MotionEvent): Float {
        val x = floatArrayOf(0f, 0f)
        val y = floatArrayOf(0f, 0f)

        for (i in 0 until pointerCount) {
            x[i] = event.getX(i)
            y[i] = event.getY(i)
        }

        val xLength = (x[0] - x[1]).absoluteValue
        val yLength = (y[0] - y[1]).absoluteValue

        return xLength * yLength
    }

    private fun getPlaylist() {
        setLoading(true)

        val query = Query.query(listOf())

        playlistViewModel.pullTrigger(Params(query))
        playlistViewModel.playlist.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.getStatus()) {
                Status.SUCCESS -> {
                    val list = resource.getData() as PagedList<PlaylistListModel.PlaylistModel>
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