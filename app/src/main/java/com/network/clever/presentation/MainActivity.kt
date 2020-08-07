package com.network.clever.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
import com.network.clever.R
import com.network.clever.presentation.auth.AuthFragment
import com.network.clever.presentation.home.TabFragment
import timber.log.Timber


class MainActivity : BaseActivity() {
    companion object {
        const val RC_SIGN_IN = 9001
    }

    override val frameLayoutId = R.id.contentFrame

    lateinit var googleSignInClient: GoogleSignInClient

    override fun setContentView() {
        setContentView(R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isValidatedUser = firebaseAuth.currentUser

        if (isValidatedUser == null) {
            addFragment(
                AuthFragment::class.java,
                BACK_STACK_STATE_NEW
            )
        } else {
            addFragment(
                TabFragment::class.java,
                BACK_STACK_STATE_NEW
            )
        }

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        Log.d("MainActivity", "googleSignIn")
        Timber.d("googleSignIn")
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MainActivity", "onActivityResult: ${requestCode}")
        Timber.d("onActivityResult: ${requestCode}")

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            Log.d("MainActivity", "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.w("MainActivity", "Google sign in failed", e)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        showLoading(true)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addFragment(TabFragment::class.java, BACK_STACK_STATE_REPLACE)

                } else {
                    val view = findViewById<View>(frameLayoutId)
                    Snackbar.make(view, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }

                showLoading(false)
            }
    }
}