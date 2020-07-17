package com.network.clever.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.meuus.base.view.setDefaultWindowTheme
import com.network.clever.R

open class LoadingDialog : DialogFragment() {
    companion object {
        fun getInstance(): LoadingDialog {
            return LoadingDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.setDefaultWindowTheme()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.DialogFullscreenTransparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return activity?.layoutInflater?.inflate(R.layout.dialog_progress, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialog?.setCancelable(false)
    }

    fun isShowing(): Boolean {
        return if (dialog != null) {
            dialog!!.isShowing
        } else {
            dismiss()
            false
        }
    }
}