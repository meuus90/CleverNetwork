package com.network.clever.data.datasource.model.setting

import com.network.clever.data.datasource.model.BaseData

data class AppSetting(
    var isRepeatChecked: Boolean = false,
    var isBackgroundPlay: Boolean = false
) : BaseData()