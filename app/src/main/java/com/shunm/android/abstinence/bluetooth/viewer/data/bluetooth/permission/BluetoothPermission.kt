package com.shunm.android.abstinence.bluetooth.viewer.data.bluetooth.permission

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi


enum class BluetoothPermission(val value: String) {
    @RequiresApi(Build.VERSION_CODES.S)
    BLUETOOTH_SCAN(Manifest.permission.BLUETOOTH_SCAN),

    @RequiresApi(Build.VERSION_CODES.S)
    BLUETOOTH_CONNECT(Manifest.permission.BLUETOOTH_CONNECT),
    BLUETOOTH_FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION)
}