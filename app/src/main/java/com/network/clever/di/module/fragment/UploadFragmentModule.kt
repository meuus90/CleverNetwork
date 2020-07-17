package com.network.clever.di.module.fragment

import com.network.clever.presentation.home.UploadFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UploadFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeUploadFragment(): UploadFragment
}