package com.network.base.di.module.fragment

import com.network.clever.presentation.auth.AuthFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeHomeFragment(): AuthFragment
}