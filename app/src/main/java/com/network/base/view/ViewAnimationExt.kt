/*
 * Copyright (C)  2020 Blue-Ocean
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.network.base.view

import android.animation.*
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart

fun TextView.cycleTextViewExpansion(
    height: Int,
    duration: Long,
    startAction: (animator: Animator) -> Unit,
    endAction: (animator: Animator) -> Unit
) {
    val animation = ObjectAnimator.ofInt(this, "height", height)
    animation.doOnStart(startAction)
    animation.doOnEnd(endAction)
    animation.setDuration(duration).start()
}


fun TextView.setSizeAnim(size: Float, duration: Long) {
    textSize
    val animation = ObjectAnimator.ofFloat(this, "textSize", size)
    animation.setDuration(duration).start()
}

fun View.expand(animListener: AnimListener) {
    val a = expandAction()
    a.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {}

        override fun onAnimationEnd(animation: Animation) {
            animListener.onFinish()
        }

        override fun onAnimationRepeat(animation: Animation) {

        }
    })
    startAnimation(a)
}

fun View.expand() {
    val a = expandAction()
    startAnimation(a)
}

fun View.expandAction(): Animation {
    measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    val targetHeight = measuredHeight

    layoutParams.height = 0
    visibility = View.VISIBLE
    val a = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            layoutParams.height = if (interpolatedTime == 1f)
                ViewGroup.LayoutParams.WRAP_CONTENT
            else
                (targetHeight * interpolatedTime).toInt()
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    a.duration =
        (targetHeight / context.resources.displayMetrics.density).toInt().toLong()
    startAnimation(a)
    return a
}

fun View.collapse() {
    val initialHeight = measuredHeight

    val a = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            if (interpolatedTime == 1f) {
                visibility = View.GONE
            } else {
                layoutParams.height =
                    initialHeight - (initialHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    a.duration =
        (initialHeight / context.resources.displayMetrics.density).toInt().toLong()
    startAnimation(a)
}

fun View.flyInUp(animListener: AnimListener? = null) {
    visibility = View.VISIBLE
    alpha = 0.0f
    translationY = height.toFloat()
    // Prepare the View for the animation
    animate()
        .setDuration(200)
        .translationY(0f)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animListener?.onFinish()
                super.onAnimationEnd(animation)
            }
        })
        .alpha(1.0f)
        .start()
}

fun View.flyOutDown(animListener: AnimListener? = null) {
    visibility = View.VISIBLE
    alpha = 1.0f
    translationY = 0f
    // Prepare the View for the animation
    animate()
        .setDuration(200)
        .translationY(height.toFloat())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animListener?.onFinish()
                super.onAnimationEnd(animation)
            }
        })
        .alpha(0.0f)
        .start()
}

fun View.fadeIn(animListener: AnimListener? = null) {
    if (visibility != View.VISIBLE) {
        show()
        alpha = 0.0f
        // Prepare the View for the animation
        animate()
            .setDuration(500)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    animListener?.onFinish()
                    super.onAnimationEnd(animation)
                }
            })
            .alpha(1.0f)
    }
}

fun View.fadeOut(animListener: AnimListener? = null) {
    if (visibility == View.VISIBLE) {
        alpha = 1.0f
        // Prepare the View for the animation
        animate()
            .setDuration(500)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    animListener?.onFinish()
                    super.onAnimationEnd(animation)
                    gone()
                }
            })
            .alpha(0.0f)
    }
}

fun View.initShowOut() {
    gone()
    translationY = height.toFloat()
    alpha = 0f
}

fun View.showIn() {
    show()
    alpha = 0f
    translationY = height.toFloat()
    animate()
        .setDuration(200)
        .translationY(0f)
        .setListener(object : AnimatorListenerAdapter() {
        })
        .alpha(1f)
        .start()
}

fun View.showOut() {
    show()
    alpha = 1f
    translationY = 0f
    animate()
        .setDuration(200)
        .translationY(height.toFloat())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
                super.onAnimationEnd(animation)
            }
        }).alpha(0f)
        .start()
}

fun View.upShow(duration: Long = 300) {
    show()
    translationY = height.toFloat()
    animate()
        .setDuration(duration)
        .translationY(0f)
        .setListener(object : AnimatorListenerAdapter() {
        })
        .start()
}

fun View.dropGone(duration: Long = 300) {
    translationY = 0f
    animate()
        .setDuration(duration)
        .translationY(height.toFloat())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                gone()
                super.onAnimationEnd(animation)
            }
        })
        .start()
}

fun View.rotateFab(rotate: Boolean): Boolean {
    animate().setDuration(200)
        .setListener(object : AnimatorListenerAdapter() {
        })
        .rotation(if (rotate) 180f else 0f)
    return rotate
}


interface AnimListener {
    fun onFinish()
}

fun View.fadeOutIn() {
    alpha = 0f
    val animatorSet = AnimatorSet()
    val animatorAlpha = ObjectAnimator.ofFloat(this, "alpha", 0f, 0.5f, 1f)
    ObjectAnimator.ofFloat(this, "alpha", 0f).start()
    animatorAlpha.duration = 500
    animatorSet.play(animatorAlpha)
    animatorSet.start()
}

fun View.showScale(animListener: AnimListener? = null) {
    animate()
        .scaleY(1f)
        .scaleX(1f)
        .setDuration(200)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animListener?.onFinish()
                super.onAnimationEnd(animation)
            }
        })
        .start()
}

fun View.hideScale(animListener: AnimListener? = null) {
    animate()
        .scaleY(0f)
        .scaleX(0f)
        .setDuration(200)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animListener?.onFinish()
                super.onAnimationEnd(animation)
            }
        })
        .start()
}

fun View.hideFab() {
    val moveY = 2 * height
    animate()
        .translationY(moveY.toFloat())
        .setDuration(300)
        .start()
}

fun View.showFab() {
    animate()
        .translationY(0f)
        .setDuration(300)
        .start()
}

fun ImageView.rotateAddOrDelete(rotate: Boolean) {
    show()
    val start = if (rotate) Color.parseColor("#38a89d") else Color.parseColor("#044b65")
    val end = if (rotate) Color.parseColor("#044b65") else Color.parseColor("#38a89d")
    val anim = ValueAnimator.ofArgb(start, end)
    anim.setEvaluator(ArgbEvaluator())
    anim.addUpdateListener {
        val fraction = if (rotate) it.animatedFraction else 1 - it.animatedFraction
        rotation = fraction * 45f
        backgroundTintList = ColorStateList.valueOf(it.animatedValue as Int)
    }
    anim.duration = 300
    anim.start()
}