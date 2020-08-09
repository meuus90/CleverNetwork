package com.network.clever.di.module.fragment

import com.network.clever.presentation.home.PlaylistListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PlaylistListFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributePlaylistListFragment(): PlaylistListFragment
}