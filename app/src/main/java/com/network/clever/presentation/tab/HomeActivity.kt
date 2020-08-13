package com.network.clever.presentation.tab

import android.os.Bundle
import com.network.clever.R
import com.network.clever.presentation.BaseActivity


class HomeActivity : BaseActivity() {
    override val frameLayoutId = R.id.contentFrame

    override fun setContentView() {
        setContentView(R.layout.activity_default)
    }

    override fun onUpdateUI() {
        super.onUpdateUI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addFragment(
            TabFragment::class.java,
            BACK_STACK_STATE_NEW
        )
    }
}