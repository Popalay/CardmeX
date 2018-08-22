package com.popalay.shaketoreport

import java.util.Date

data class BugReport(
    val description: String = "",
    val screenshot: String = "",
    val deviceInfo: DeviceInfo? = null,
    val createdAt: Date? = null
)