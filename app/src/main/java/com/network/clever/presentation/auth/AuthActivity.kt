package com.network.clever.presentation.auth

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
import com.network.clever.presentation.BaseActivity
import com.network.clever.presentation.Caller
import timber.log.Timber


class AuthActivity : BaseActivity() {
    companion object {
        const val RC_SIGN_IN = 9001
    }

    override val frameLayoutId = R.id.contentFrame

    lateinit var googleSignInClient: GoogleSignInClient

    override fun setContentView() {
        setContentView(R.layout.activity_default)
    }

    override fun onUpdateUI() {
        super.onUpdateUI()
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
            Caller.openMyPlaylist(this)
        }

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        Log.d("MainActivity", "googleSignIn")
        Timber.d("googleSignIn")
        startActivityForResult(
            signInIntent,
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.d("onActivityResult: ${requestCode}")

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!

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
                    localStorage.setAuthToken(idToken)
                    Caller.openMyPlaylist(this)
                } else {
                    val view = findViewById<View>(frameLayoutId)
                    Snackbar.make(view, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }

                showLoading(false)
            }
    }
}