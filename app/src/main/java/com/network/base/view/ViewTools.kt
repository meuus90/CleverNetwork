package com.bluewhale.base.view.extension

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.NestedScrollView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat


class ViewTools {
    companion object {
        fun copyToClipboard(context: Context, data: String) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("clipboard", data)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        fun nestedScrollTo(nested: NestedScrollView, targetView: View) {
            nested.post(Runnable { nested.scrollTo(500, targetView.bottom) })
        }

        fun dip2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        fun px2dip(context: Context, pxValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }

        fun toggleArrow(view: View): Boolean {
            return if (view.rotation == 0f) {
                view.animate().setDuration(200).rotation(180f)
                true
            } else {
                view.animate().setDuration(200).rotation(0f)
                false
            }
        }

        fun toggleArrow(show: Boolean, view: View): Boolean {
            return toggleArrow(
                show,
                view,
                true
            )
        }

        fun toggleArrow(show: Boolean, view: View, delay: Boolean): Boolean {
            return if (show) {
                view.animate().setDuration((if (delay) 200 else 0).toLong()).rotation(180f)
                true
            } else {
                view.animate().setDuration((if (delay) 200 else 0).toLong()).rotation(0f)
                false
            }
        }

        fun getBitmapFromDrawable(context: Context, drawableId: Int): Bitmap {
            val drawable = AppCompatResources.getDrawable(context, drawableId)

            if (drawable is BitmapDrawable) {
                return (drawable as BitmapDrawable).bitmap
            } else if (drawable is VectorDrawableCompat || drawable is VectorDrawable) {
                val bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)

                return bitmap
            } else {
                throw IllegalArgumentException("unsupported drawable type")
            }
        }
    }
}

fun Float.fromDpToPixel(context: Context): Int {
    return (this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT.toFloat())).toInt()
}