package com.network.clever.di.module.activity

import com.network.clever.di.module.fragment.HomeFragmentModule
import com.network.clever.di.module.fragment.PlaylistListFragmentModule
import com.network.clever.di.module.fragment.SettingFragmentModule
import com.network.clever.di.module.fragment.TabFragmentModule
import com.network.clever.presentation.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(
        modules = [
            TabFragmentModule::class,
            HomeFragmentModule::class,
            PlaylistListFragmentModule::class,
            SettingFragmentModule::class
        ]
    )
    internal abstract fun contributeHomeActivity(): HomeActivity
}