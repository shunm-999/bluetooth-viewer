package com.shunm.android.abstinence.bluetooth.viewer.data.permission

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object BluetoothPermissionChecker {
    fun hasScanPermission(context: Context): Boolean {
        return if (isVersionEqualOrAboveS()) {
            ContextCompat.checkSelfPermission(
                context,
                BluetoothPermission.BLUETOOTH_SCAN.value
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context,
                BluetoothPermission.BLUETOOTH_FINE_LOCATION.value
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun hasConnectPermission(context: Context): Boolean {
        return if (isVersionEqualOrAboveS()) {
            ContextCompat.checkSelfPermission(
                context,
                BluetoothPermission.BLUETOOTH_CONNECT.value
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun isVersionEqualOrAboveS(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }
}