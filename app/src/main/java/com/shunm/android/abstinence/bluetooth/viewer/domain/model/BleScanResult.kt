package com.shunm.android.abstinence.bluetooth.viewer.domain.model

data class BleScanResult(
    val bleDevice: BleDevice,
    val rssi: Int
)