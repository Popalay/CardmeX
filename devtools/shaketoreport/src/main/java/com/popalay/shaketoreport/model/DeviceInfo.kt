package com.popalay.shaketoreport.model

data class DeviceInfo(
    val device: String,
    val androidVersion: String,
    val sdkVersion: String,
    val display: String
) {
    override fun toString(): String =
        """Device: $device
        |Display: $display
        |Android: $androidVersion
        |SDK: $sdkVersion""".trimMargin()
}