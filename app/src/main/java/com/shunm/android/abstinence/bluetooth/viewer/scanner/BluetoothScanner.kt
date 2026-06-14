package com.shunm.android.abstinence.bluetooth.viewer.scanner

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService

class DeviceBluetoothScanner {

    fun getBluetoothScanResult(context: Context): DeviceBluetoothScanResult {
        val bluetoothManager = getSystemService(context, BluetoothManager::class.java)
        val bluetoothAdapter =
            bluetoothManager?.adapter ?: return DeviceBluetoothScanResult.Unavailable
        return if (bluetoothAdapter.isEnabled) {
            DeviceBluetoothScanResult.Available(bluetoothAdapter)
        } else {
            DeviceBluetoothScanResult.Disabled
        }
    }
}

sealed interface DeviceBluetoothScanResult {
    data class Available(private val adapter: BluetoothAdapter) : DeviceBluetoothScanResult
    data object Disabled : DeviceBluetoothScanResult
    data object Unavailable : DeviceBluetoothScanResult
}