package com.network.clever.di.module.fragment

import com.network.clever.presentation.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeHomeFragment(): HomeFragment
}