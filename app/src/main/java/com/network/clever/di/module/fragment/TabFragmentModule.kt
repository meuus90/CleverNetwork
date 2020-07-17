package com.network.clever.di.module.fragment

import com.network.clever.presentation.home.TabFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class TabFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeTabFragment(): TabFragment
}