package com.popalay.shaketoreport

import java.util.concurrent.TimeUnit

data class Config(
    val isEnabled: Boolean,
    val reportTimeout: ReportTimeout
)

data class ReportTimeout(
    val time: Long,
    val timeUnit: TimeUnit
)