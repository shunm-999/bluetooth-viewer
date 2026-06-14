package com.shunm.android.abstinence.bluetooth.viewer.scanner

import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService

object BluetoothAvailabilityChecker {
    fun getAvailability(context: Context): BluetoothAvailability {

        val bluetoothManager = getSystemService(context, BluetoothManager::class.java)
        val bluetoothAdapter =
            bluetoothManager?.adapter ?: return BluetoothAvailability.Unavailable

        return if (bluetoothAdapter.isEnabled) {
            BluetoothAvailability.Available
        } else {
            BluetoothAvailability.Disabled
        }
    }
}

sealed interface BluetoothAvailability {
    data object Available : BluetoothAvailability
    data object Disabled : BluetoothAvailability
    data object Unavailable : BluetoothAvailability
}