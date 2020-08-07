package com.network.clever.constant

import android.os.Build
import androidx.multidex.BuildConfig
import org.junit.Assert
import org.junit.Test


class AppConfigTest {
    @Test
    fun test_isProductionVersion() {
        Assert.assertEquals(BuildConfig.FLAVOR == "prod", AppConfig.isProductionVersion)
    }

    @Test
    fun test_isDevVersion() {
        Assert.assertEquals(BuildConfig.FLAVOR == "dev", AppConfig.isDevVersion)
    }

    @Test
    fun test_isMockVersion() {
        Assert.assertEquals(BuildConfig.FLAVOR == "mock", AppConfig.isMockVersion)
    }

    @Test
    fun test_isTypeDebug() {
        Assert.assertEquals(BuildConfig.BUILD_TYPE == "debug", AppConfig.isTypeDebug)
    }

    @Test
    fun test_isTypeRelease() {
        Assert.assertEquals(BuildConfig.BUILD_TYPE == "release", AppConfig.isTypeRelease)
    }
}