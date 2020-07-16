package com.network.clever.presentation.dialog

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.network.base.view.gone
import com.network.base.view.setDefaultWindowTheme
import com.network.base.view.show
import com.network.clever.R
import kotlinx.android.synthetic.main.dialog_default.*
import java.util.*

class DefaultDialog(private val mContext: Context, private val isHorizontal: Boolean = true) :
    androidx.fragment.app.DialogFragment() {
    var mIsCloseButton = false
    var mTitle: String? = null
    var mMessage: String? = null
    var mCustomViews: ArrayList<View> = arrayListOf()

    var mPositive: String = ""
    var mPositiveButtonListener: DialogInterface.OnClickListener? = null
    var mNegative: String = ""
    var mNegativeButtonListener: DialogInterface.OnClickListener? = null

    var mOnDismissListener: DialogInterface.OnDismissListener? = null
    var mOnCancelListener: DialogInterface.OnCancelListener? = null

    var isShowing = false

    override fun onStart() {
        super.onStart()
        dialog?.setDefaultWindowTheme()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return activity?.layoutInflater?.inflate(R.layout.dialog_default, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (isHorizontal) {
            v_button_container.gone()
            h_button_container.show()
        } else {
            h_button_container.gone()
            v_button_container.show()
        }

        setCloseButton()
        setTitle()
        setMessage()
        setView()
        setPositiveButton()
        setNegativeButton()

        view?.setOnClickListener { dismiss() }

        iv_close.setOnClickListener {
            dismiss()
        }
    }


    fun setCloseButton() {
        if (mIsCloseButton)
            iv_close.show()
        else
            iv_close.gone()
    }

    fun setTitle() {
        if (mTitle != null) {
            tv_title.show()
            tv_title.text = mTitle
        } else {
            tv_title.gone()
        }
    }

    fun setMessage() {
        if (mMessage != null) {
            tv_message.show()
            tv_message.text = mMessage
        } else {
            tv_message.gone()
        }
    }

    fun setView() {
        for (view in mCustomViews) {
            view.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            v_contents.addView(view)
        }
    }

    fun setPositiveButton() {
        if (isHorizontal) {
            tv_h_positive.text = mPositive
            tv_h_positive.setOnClickListener {
                mPositiveButtonListener?.onClick(this.dialog, DialogInterface.BUTTON_POSITIVE)
                dismiss()
            }
        } else {
            tv_v_positive.text = mPositive
            tv_v_positive.setOnClickListener {
                mPositiveButtonListener?.onClick(this.dialog, DialogInterface.BUTTON_POSITIVE)
                dismiss()
            }
        }

    }

    fun setNegativeButton() {
        if (isHorizontal) {
            if (mNegativeButtonListener != null) {
                tv_h_negative.show()
                v_h_center_bar.show()

                tv_h_negative.text = mNegative
                tv_h_negative.setOnClickListener {
                    mNegativeButtonListener?.onClick(this.dialog, DialogInterface.BUTTON_NEGATIVE)
                    dismiss()
                }
            } else {
                tv_h_negative.gone()
                v_h_center_bar.gone()
            }
        } else {
            if (mNegativeButtonListener != null) {
                tv_v_negative.show()
                v_v_center_bar.show()

                tv_v_negative.text = mNegative
                tv_v_negative.setOnClickListener {
                    mNegativeButtonListener?.onClick(this.dialog, DialogInterface.BUTTON_NEGATIVE)
                    dismiss()
                }
            } else {
                tv_v_negative.gone()
                v_v_center_bar.gone()
            }
        }
    }

    private fun runMailChooser(mailTo: String) {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf(mailTo))
        try {
            startActivity(Intent.createChooser(i, "Choose Mail Client"))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this.mContext,
                "There is no email client installed.", Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mOnDismissListener?.onDismiss(dialog)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        mOnCancelListener?.onCancel(dialog)
    }

    class Builder(val context: Context, isHorizontal: Boolean = true) {
        var d: DefaultDialog = DefaultDialog(context, isHorizontal)

        fun setCloseButton(isCloseButton: Boolean): Builder {
            d.mIsCloseButton = isCloseButton

            if (d.isShowing)
                d.setCloseButton()

            return this
        }

        fun setTitle(text: CharSequence): Builder {
            d.mTitle = text.toString()

            if (d.isShowing)
                d.setTitle()
            return this
        }

        fun setTitle(@StringRes textId: Int): Builder {
            d.mTitle = context.getString(textId)

            if (d.isShowing)
                d.setTitle()
            return this
        }

        fun setMessage(text: CharSequence): Builder {
            d.mMessage = text.toString()

            if (d.isShowing)
                d.setMessage()
            return this
        }

        fun setMessage(@StringRes textId: Int): Builder {
            d.mMessage = context.getString(textId)

            if (d.isShowing)
                d.setMessage()
            return this
        }

        fun setView(view: View): Builder {
            d.mCustomViews.add(view)

            if (d.isShowing)
                d.setView()

            return this
        }

        fun setPositiveButton(
            text: CharSequence,
            listener: (DialogInterface, Int) -> Unit
        ): Builder {
            d.mPositive = text.toString()
            d.mPositiveButtonListener = DialogInterface.OnClickListener(listener)

            if (d.isShowing)
                d.setPositiveButton()
            return this
        }

        fun setPositiveButton(
            @StringRes textId: Int,
            listener: (DialogInterface, Int) -> Unit
        ): Builder {
            d.mPositive = context.getString(textId)
            d.mPositiveButtonListener = DialogInterface.OnClickListener(listener)

            if (d.isShowing)
                d.setPositiveButton()
            return this
        }

        fun setNegativeButton(
            text: CharSequence,
            listener: (DialogInterface, Int) -> Unit
        ): Builder {
            d.mNegative = text.toString()
            d.mNegativeButtonListener = DialogInterface.OnClickListener(listener)

            if (d.isShowing)
                d.setNegativeButton()
            return this
        }

        fun setNegativeButton(
            @StringRes textId: Int,
            listener: (DialogInterface, Int) -> Unit
        ): Builder {
            d.mNegative = context.getString(textId)
            d.mNegativeButtonListener = DialogInterface.OnClickListener(listener)

            if (d.isShowing)
                d.setNegativeButton()
            return this
        }

        fun setOnDismissListener(onDismiss: (DialogInterface) -> Unit): Builder {
            d.mOnDismissListener = DialogInterface.OnDismissListener(onDismiss)
            return this
        }

        fun setOnDismissListener(onDismiss: DialogInterface.OnDismissListener? = null): Builder {
            d.mOnDismissListener = onDismiss
            return this
        }

        fun setOnCancelListener(onCancel: (DialogInterface) -> Unit): Builder {
            d.mOnCancelListener = DialogInterface.OnCancelListener(onCancel)
            return this
        }

        fun setOnCancelListener(onCancel: DialogInterface.OnCancelListener? = null): Builder {
            d.mOnCancelListener = onCancel
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            d.isCancelable = cancelable
            return this
        }

        fun show(manager: FragmentManager): DefaultDialog {
            d.show(manager, null)
            d.isShowing = true
            return d
        }

        fun show(transaction: FragmentTransaction): DefaultDialog {
            d.show(transaction, null)
            d.isShowing = true
            return d
        }
    }
}