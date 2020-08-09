package com.network.clever.presentation.stream

import android.os.Bundle
import com.network.clever.R
import com.network.clever.presentation.BaseActivity


class PlayerActivity : BaseActivity() {
    override val frameLayoutId = R.id.contentFrame

    override fun setContentView() {
        setContentView(R.layout.activity_default)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addFragment(
            PlayerFragment::class.java,
            BACK_STACK_STATE_NEW
        )
    }
}