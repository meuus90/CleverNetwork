package com.network.clever.presentation

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.network.clever.R
import com.network.clever.presentation.auth.AuthFragment
import com.network.clever.presentation.home.TabFragment
import javax.inject.Inject


class MainActivity : BaseActivity() {
    override val frameLayoutId = R.id.contentFrame

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun setContentView() {
        setContentView(R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isValidatedUser = firebaseAuth.currentUser

        if (isValidatedUser == null) {
            addFragment(
                AuthFragment::class.java,
                BACK_STACK_STATE_REPLACE
            )
        } else {
            addFragment(
                TabFragment::class.java,
                BACK_STACK_STATE_REPLACE
            )
        }
    }
}