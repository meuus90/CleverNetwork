package com.network.clever.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.network.base.view.setDefaultWindowTheme
import com.network.clever.R
import kotlinx.android.synthetic.main.dialog_sign_up.*

class SignUpDialog(val onConfirm: (email: String, pw: String) -> Unit) : DialogFragment() {
    override fun onStart() {
        super.onStart()
        dialog?.setDefaultWindowTheme()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return activity?.layoutInflater?.inflate(R.layout.dialog_sign_up, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tv_confirm.setOnClickListener{
            val email = et_email.text.toString()
            val pw = et_pw.text.toString()
            onConfirm(email, pw)
            dismiss()
        }
    }
}