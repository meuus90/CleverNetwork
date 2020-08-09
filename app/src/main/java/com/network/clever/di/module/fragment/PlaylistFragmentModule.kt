package com.network.clever.di.module.fragment

import com.network.clever.presentation.playlist.PlaylistFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PlaylistFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributePlaylistFragment(): PlaylistFragment
}