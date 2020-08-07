package com.network.clever.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
import com.network.clever.R
import com.network.clever.constant.AppConfig
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

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(AppConfig.clientId)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
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

        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

            if (result?.isSuccess == true) {
                Log.d("MainActivity", "Google sign in success")
                Timber.e("Google sign in success")

                result.signInAccount?.let {
                    firebaseAuthWithGoogle(it)
                }
            } else {
                Log.d("MainActivity", "Google sign in failed")
                Timber.e("Google sign in failed")
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        showLoading(true)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
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