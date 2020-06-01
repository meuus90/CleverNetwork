package com.network.base.utility

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<in P, T> : ViewModel() {
    abstract fun pullTrigger(params: P)
}