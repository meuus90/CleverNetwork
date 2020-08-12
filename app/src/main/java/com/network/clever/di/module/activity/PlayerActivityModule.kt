package com.network.clever.di.module.activity

import com.network.clever.presentation.player.PlayerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PlayerActivityModule {
    @ContributesAndroidInjector(
        modules = []
    )
    internal abstract fun contributePlayerActivity(): PlayerActivity
}