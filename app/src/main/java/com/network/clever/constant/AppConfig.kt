package com.network.clever.constant

import androidx.multidex.BuildConfig


object AppConfig {
    val isProductionVersion: Boolean
        get() = BuildConfig.FLAVOR.contains("prod") && !BuildConfig.DEBUG

    val isDevVersion: Boolean
        get() = BuildConfig.FLAVOR.contains("dev")

    val isMockVersion: Boolean
        get() = BuildConfig.FLAVOR.contains("mock")

    val isTypeDebug: Boolean
        get() = BuildConfig.BUILD_TYPE.contains("debug")

    val isTypeRelease: Boolean
        get() = BuildConfig.BUILD_TYPE.contains("release")

    val clientId: String
        get() = "691975443500-449ud3lb4i1kt5bvvhvit861aau43286.apps.googleusercontent.com"
}