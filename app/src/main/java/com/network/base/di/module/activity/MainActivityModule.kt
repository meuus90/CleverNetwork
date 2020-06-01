package com.network.base.di.module.activity

import com.network.base.di.module.fragment.AuthFragmentModule
import com.network.base.di.module.fragment.HomeFragmentModule
import com.network.clever.presentation.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [HomeFragmentModule::class, AuthFragmentModule::class])
    internal abstract fun contributeMainActivity(): MainActivity
}