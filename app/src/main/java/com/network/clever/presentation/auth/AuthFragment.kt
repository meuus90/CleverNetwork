package com.network.clever.presentation.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meuus.base.view.AutoClearedValue
import com.network.clever.R
import com.network.clever.presentation.BaseFragment
import com.network.clever.presentation.MainActivity
import kotlinx.android.synthetic.main.fragment_auth.*
import timber.log.Timber

class AuthFragment : BaseFragment() {
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
                inflater.inflate(R.layout.fragment_auth, container, false)
            )
        return acvView.get()?.rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tv_sign_in.setOnClickListener {
            Log.d(this.tag, "setOnClickListener")
            Timber.d("setOnClickListener")

            mainActivity.googleSignIn()
        }
    }
}