package com.network.clever.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseUser
import com.network.base.view.AutoClearedValue
import com.network.clever.R
import com.network.clever.presentation.BaseActivity
import com.network.clever.presentation.BaseFragment
import com.network.clever.presentation.MainActivity
import com.network.clever.presentation.home.HomeFragment
import kotlinx.android.synthetic.main.fragment_auth.*

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

    var email = ""
    var pw = ""
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        et_email.addTextChangedListener {
            email = it.toString()
        }

        et_pw.addTextChangedListener {
            pw = it.toString()
        }

        tv_sign_in.setOnClickListener {
            if (email.isNotEmpty() && pw.isNotEmpty())
                mainActivity.firebaseAuth.signInWithEmailAndPassword(email, pw)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
//                        Log.d(TAG, "createUserWithEmail:success")
                            mainActivity.firebaseAuth.currentUser?.let {
                                updateUI(it)
                            }
                        } else {
                            // If sign in fails, display a message to the user.
//                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                context, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
        }

        tv_sign_up.setOnClickListener {
            if (email.isNotEmpty() && pw.isNotEmpty())
                mainActivity.firebaseAuth.createUserWithEmailAndPassword(email, pw)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                context, "Sign up success",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // If sign in fails, display a message to the user.
//                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                context, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
        }
    }

    fun updateUI(user: FirebaseUser) {
        addFragment(HomeFragment::class.java, BaseActivity.BACK_STACK_STATE_REPLACE)
    }
}