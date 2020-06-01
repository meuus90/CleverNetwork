package com.network.clever.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.network.base.view.AutoClearedValue
import com.network.clever.R
import com.network.clever.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment : BaseFragment() {
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

    var id = ""
    var pw = ""
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        et_id.addTextChangedListener {
            id = it.toString()
        }

        et_pw.addTextChangedListener {
            pw = it.toString()
        }

        tv_sign_in.setOnClickListener {

        }

        tv_sign_up.setOnClickListener {

        }
    }
}