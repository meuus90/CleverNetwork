package com.network.clever.di.module.activity

import com.network.clever.di.module.fragment.AuthFragmentModule
import com.network.clever.presentation.auth.AuthActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthActivityModule {
    @ContributesAndroidInjector(
        modules = [
            AuthFragmentModule::class
        ]
    )
    internal abstract fun contributeMainActivity(): AuthActivity
}