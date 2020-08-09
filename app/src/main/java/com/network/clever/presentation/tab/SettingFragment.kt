package com.network.clever.presentation.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meuus.base.view.AutoClearedValue
import com.network.clever.R
import com.network.clever.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : BaseFragment() {
    companion object {
        fun newInstance() = SettingFragment().apply {
            arguments = Bundle(1).apply {
                putString(FRAGMENT_TAG, SettingFragment::class.java.name)
            }
        }
    }

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
                inflater.inflate(R.layout.fragment_setting, container, false)
            )
        return acvView.get()?.rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tv_logout.setOnClickListener {
            homeActivity.firebaseAuth.signOut()
            homeActivity.localStorage.logOut()
        }
    }
}