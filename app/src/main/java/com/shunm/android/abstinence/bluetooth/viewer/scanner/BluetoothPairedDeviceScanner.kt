package com.shunm.android.abstinence.bluetooth.viewer.scanner

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService

class BluetoothPairedDeviceScanner {

    fun getDevices(context: Context): BluetoothPairedDeviceScannerResult {
        if (!BluetoothPermissionChecker.checkBluetoothConnectPermission(context)) {
            return BluetoothPairedDeviceScannerResult.Failed
        }

        val bluetoothManager = getSystemService(context, BluetoothManager::class.java)
            ?: return BluetoothPairedDeviceScannerResult.Failed
        val bluetoothAdapter =
            bluetoothManager.adapter ?: return BluetoothPairedDeviceScannerResult.Failed

        return try {
            val pairedDevices = bluetoothAdapter.bondedDevices
            BluetoothPairedDeviceScannerResult.Success(pairedDevices.toList())
        } catch (e: SecurityException) {
            BluetoothPairedDeviceScannerResult.Failed
        }
    }

    fun startDiscovery(context: Context) {
        if (!BluetoothPermissionChecker.checkBluetoothScanPermission(context)) {
            return
        }

        val bluetoothManager = getSystemService(context, BluetoothManager::class.java)
            ?: return
        val bluetoothAdapter =
            bluetoothManager.adapter ?: return

        try {
            bluetoothAdapter.startDiscovery()
        } catch (e: SecurityException) {
            // nop
        }
    }
}

sealed interface BluetoothPairedDeviceScannerResult {
    data class Success(val devices: List<BluetoothDevice>) : BluetoothPairedDeviceScannerResult
    data object Failed : BluetoothPairedDeviceScannerResult
}