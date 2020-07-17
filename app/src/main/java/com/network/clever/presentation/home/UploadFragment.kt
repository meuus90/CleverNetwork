package com.network.clever.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meuus.base.view.AutoClearedValue
import com.network.clever.R
import com.network.clever.presentation.BaseFragment
import com.network.clever.presentation.MainActivity

class UploadFragment : BaseFragment() {
    companion object {
        fun newInstance() = UploadFragment().apply {
            arguments = Bundle(1).apply {
                putString(FRAGMENT_TAG, UploadFragment::class.java.name)
            }
        }
    }

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
                inflater.inflate(R.layout.fragment_upload, container, false)
            )
        return acvView.get()?.rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}