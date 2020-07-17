package com.network.clever.presentation

import android.content.Context
import androidx.fragment.app.Fragment
import com.network.clever.di.Injectable

open class BaseFragment : Fragment(), Injectable {
    lateinit var baseActivity: BaseActivity
    private lateinit var context: Context
    override fun getContext() = context

    override fun onAttach(context: Context) {
        super.onAttach(context)

        baseActivity = (context as BaseActivity)
        this.context = context
    }

    private fun getScreenName(): String {
        return this::class.java.simpleName
    }

    internal fun addFragment(cls: Class<*>, backStackState: Int): Fragment {
        return baseActivity.addFragment(cls, backStackState)
    }

    internal fun goToRootFragment() {
        baseActivity.goToRootFragment()
    }
}