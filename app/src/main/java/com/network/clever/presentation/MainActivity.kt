package com.network.clever.presentation

import android.os.Bundle
import com.network.clever.R
import com.network.clever.presentation.home.HomeFragment


class MainActivity : BaseActivity() {
    override val frameLayoutId = R.id.contentFrame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragmentInActivity(
            HomeFragment(),
                frameLayoutId
        )
    }

    override fun setContentView() {
        setContentView(R.layout.activity_main)
    }
}