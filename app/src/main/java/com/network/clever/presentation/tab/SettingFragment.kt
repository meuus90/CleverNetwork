package com.network.clever.presentation.tab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meuus.base.view.AutoClearedValue
import com.network.clever.BuildConfig
import com.network.clever.R
import com.network.clever.data.datasource.model.setting.AppSetting
import com.network.clever.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : BaseFragment() {
    companion object {
        fun newInstance() = SettingFragment().apply {
            arguments = Bundle(1).apply {
                putString(FRAGMENT_TAG, SettingFragment::class.java.name)
            }
        }
    }

    private val homeActivity: HomeActivity by lazy {
        activity as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val acvView =
            AutoClearedValue(
                this,
                inflater.inflate(R.layout.fragment_setting, container, false)
            )
        return acvView.get()?.rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        appSetting = homeActivity.localStorage.getAppSetting()

        sw_repeat.isChecked = appSetting.isRepeatChecked
        sw_background_play.isChecked = appSetting.isBackgroundPlay

        v_repeat.setOnClickListener {
            sw_repeat.isChecked = !appSetting.isRepeatChecked
        }

        sw_repeat.setOnCheckedChangeListener { buttonView, isChecked ->
            appSetting.isRepeatChecked = isChecked
            updateAppSetting()
        }

        v_background_play.setOnClickListener {
            sw_background_play.isChecked = !appSetting.isBackgroundPlay
        }

        sw_background_play.setOnCheckedChangeListener { buttonView, isChecked ->
            appSetting.isBackgroundPlay = isChecked
            updateAppSetting()
        }

        tv_version.text = BuildConfig.VERSION_NAME

        tv_clear_cache.setOnClickListener {
            homeActivity.localStorage.clearCache()

            appSetting = homeActivity.localStorage.getAppSetting()
            sw_repeat.isChecked = appSetting.isRepeatChecked
            sw_background_play.isChecked = appSetting.isBackgroundPlay

            homeActivity.audioServiceInterface.setAppSetting(appSetting)
        }

        tv_logout.setOnClickListener {
            homeActivity.localStorage.clearCache()
            appSetting = homeActivity.localStorage.getAppSetting()
            sw_repeat.isChecked = appSetting.isRepeatChecked
            sw_background_play.isChecked = appSetting.isBackgroundPlay

            homeActivity.audioServiceInterface.setAppSetting(appSetting)

            homeActivity.firebaseAuth.signOut()
            homeActivity.localStorage.logOut()
        }
    }

    private lateinit var appSetting: AppSetting

    fun updateAppSetting() {
        homeActivity.audioServiceInterface.setAppSetting(appSetting)

        Log.e("AppSetting changed : ", appSetting.toString())
        homeActivity.localStorage.setAppSetting(appSetting)
    }

}