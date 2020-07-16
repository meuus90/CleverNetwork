package com.network.base.view

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.SystemClock
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout

fun Window.setSystemBarTextWhite() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val view = findViewById<View>(android.R.id.content)
        val flags = view.systemUiVisibility
        view.systemUiVisibility = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
}

fun Window.setSystemBarTextDark() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val view = findViewById<View>(android.R.id.content)
        val flags = view.systemUiVisibility
        view.systemUiVisibility = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

fun Dialog.setDefaultWindowTheme() {
    window?.apply {
        setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        statusBarColor = Color.TRANSPARENT

        setSystemBarTextDark()
        setDimAmount(0.3f)
    }
}

fun View.setSafeOnClickListener(interval: Int = 500, onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener(interval = interval) {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

class SafeClickListener(
    private val interval: Int,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked > interval) {
            lastTimeClicked = SystemClock.elapsedRealtime()
            onSafeCLick(v)
        }
    }
}

fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

/**
 * Hide the view. (visibility = View.INVISIBLE)
 */
fun View.hide(): View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
    return this
}

/**
 * Remove the view (visibility = View.GONE)
 */
fun View.gone(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

/**
 * Extension method to show a keyboard for View.
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

/**
 * Try to hide the keyboard and returns whether it worked
 * https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    this.clearFocus()
    return false
}

/**
 * Set view height by width
 */
fun View.setHeightByRatio(widthRatio: Int, heightRatio: Int) {
    val params = layoutParams
    val mHeight = (width.toFloat() / widthRatio.toFloat()) * heightRatio.toFloat()
    params.height = mHeight.toInt()
    layoutParams = params
}

fun View.setWidth(width: Int) {
    val params = layoutParams
    params.width = width
    layoutParams = params
}

fun View.setHeight(height: Int) {
    val params = layoutParams
    params.height = height
    layoutParams = params
}

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()