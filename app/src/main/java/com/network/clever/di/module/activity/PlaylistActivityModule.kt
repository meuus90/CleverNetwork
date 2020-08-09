package com.network.clever.di.module.activity

import com.network.clever.di.module.fragment.PlaylistFragmentModule
import com.network.clever.presentation.playlist.PlaylistActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PlaylistActivityModule {
    @ContributesAndroidInjector(
        modules = [
            PlaylistFragmentModule::class
        ]
    )
    internal abstract fun contributePlaylistActivity(): PlaylistActivity
}