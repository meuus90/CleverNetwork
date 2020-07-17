package com.network.clever.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.meuus.base.view.AutoClearedValue
import com.network.clever.R
import com.network.clever.presentation.BaseFragment
import com.network.clever.presentation.MainActivity
import kotlinx.android.synthetic.main.fragment_tab.*

class TabFragment : BaseFragment() {
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
                inflater.inflate(R.layout.fragment_tab, container, false)
            )
        return acvView.get()?.rootView
    }

    private val fragments = listOf(
        HomeFragment.newInstance(),
        UploadFragment.newInstance(),
        SettingFragment.newInstance()
    )

    private lateinit var fragmentAdapter: FragmentStateAdapter
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fragmentAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }

        container.adapter = fragmentAdapter

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    container.setCurrentItem(0, true)
                    true
                }
                R.id.action_upload -> {
                    container.setCurrentItem(1, true)
                    true
                }
                R.id.action_setting -> {
                    container.setCurrentItem(2, true)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}