package com.popalay.shaketoreport

import com.google.firebase.Timestamp

data class BugReport(
    val description: String = "",
    val screenshot: String = "",
    val deviceInfo: DeviceInfo? = null,
    val createdAt: Timestamp? = null
)