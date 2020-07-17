package com.network.clever.presentation.auth

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.meuus.base.view.AutoClearedValue
import com.network.clever.R
import com.network.clever.presentation.BaseActivity
import com.network.clever.presentation.BaseFragment
import com.network.clever.presentation.MainActivity
import com.network.clever.presentation.home.TabFragment
import kotlinx.android.synthetic.main.fragment_auth.*
import timber.log.Timber
import java.util.regex.Matcher
import java.util.regex.Pattern

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
            val email = et_email.text.toString()
            val pw = et_pw.text.toString()
            Timber.e("email : $email")
            Timber.e("pw : $pw")

            if (isValidFormat(email, pw)) {
                showLoading(true)
                Timber.e("Sign in")
                mainActivity.firebaseAuth.signInWithEmailAndPassword(email, pw)
                    .addOnCompleteListener { task ->
                        showLoading(false)
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
//                        Log.d(TAG, "createUserWithEmail:success")
                            mainActivity.firebaseAuth.currentUser?.let {
                                updateUI()
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
        }

        tv_sign_up.setOnClickListener {
            val email = et_email.text.toString()
            val pw = et_pw.text.toString()
            Timber.e("email : $email")
            Timber.e("pw : $pw")

            if (isValidFormat(email, pw)) {
                showLoading(true)
                Timber.e("Sign up")
                mainActivity.firebaseAuth.createUserWithEmailAndPassword(email, pw)
                    .addOnCompleteListener { task ->
                        showLoading(false)
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
    }

    private fun isValidFormat(email: String, pw: String): Boolean {
        val checkEmail = isValidEmail(email)
        val checkPw = isValidPw(pw)
        return checkEmail /*&& checkPw*/
    }

    private fun isValidEmail(target: String): Boolean {
        return if (TextUtils.isEmpty(target))
            false
        else
            android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun isValidPw(target: String): Boolean {
        val p: Pattern = Pattern.compile("(^.*(?=.{6,100})(?=.*[0-9])(?=.*[a-zA-Z]).*$)")
        val m: Matcher = p.matcher(target)
        return m.find()
    }

    fun updateUI() {
        addFragment(TabFragment::class.java, BaseActivity.BACK_STACK_STATE_REPLACE)
    }
}