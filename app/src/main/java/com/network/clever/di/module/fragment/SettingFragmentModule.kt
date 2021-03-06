package com.network.clever.di.module.fragment

import com.network.clever.presentation.tab.SettingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeSettingFragment(): SettingFragment
}