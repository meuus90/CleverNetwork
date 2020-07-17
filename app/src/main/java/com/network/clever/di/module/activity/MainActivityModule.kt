package com.network.clever.di.module.activity

import com.network.clever.di.module.fragment.*
import com.network.clever.presentation.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(
        modules = [
            AuthFragmentModule::class,
            TabFragmentModule::class,
            HomeFragmentModule::class,
            UploadFragmentModule::class,
            SettingFragmentModule::class
        ]
    )
    internal abstract fun contributeMainActivity(): MainActivity
}