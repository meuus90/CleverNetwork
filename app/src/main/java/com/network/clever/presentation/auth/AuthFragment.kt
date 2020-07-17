package com.network.clever.presentation.auth

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.meuus.base.view.AutoClearedValue
import com.network.clever.R
import com.network.clever.presentation.BaseActivity
import com.network.clever.presentation.BaseFragment
import com.network.clever.presentation.MainActivity
import com.network.clever.presentation.home.HomeFragment
import kotlinx.android.synthetic.main.fragment_auth.*
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
            if (isValidFormat(email, pw))
                mainActivity.firebaseAuth.signInWithEmailAndPassword(email, pw)
                    .addOnCompleteListener { task ->
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

        tv_sign_up.setOnClickListener {
            if (isValidFormat(email, pw))
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

    private fun isValidFormat(email: String, pw: String): Boolean {
        return isValidEmail(email) && isValidPw(pw)
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
        return m.find() && !target.matches(Regex(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"))
    }

    fun updateUI() {
        addFragment(HomeFragment::class.java, BaseActivity.BACK_STACK_STATE_REPLACE)
    }
}