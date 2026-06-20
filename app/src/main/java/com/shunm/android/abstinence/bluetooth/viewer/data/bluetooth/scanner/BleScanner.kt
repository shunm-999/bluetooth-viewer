package com.shunm.android.abstinence.bluetooth.viewer.data.bluetooth.scanner

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import androidx.annotation.RequiresPermission
import com.shunm.android.abstinence.bluetooth.viewer.data.bluetooth.BluetoothAdapterProvider
import com.shunm.android.abstinence.bluetooth.viewer.domain.model.BleError
import com.shunm.android.abstinence.bluetooth.viewer.domain.model.BleException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BleScanner(
    private val adapterProvider: BluetoothAdapterProvider,
    private val context: Context,
) {

    @RequiresPermission(android.Manifest.permission.BLUETOOTH_SCAN)
    fun scan(): Flow<ScanResult> = callbackFlow {
        val scanner = adapterProvider.getAdapter(context)
            .unwrapOrNull()
            ?.bluetoothLeScanner
            ?: run {
                close(BleException(BleError.BluetoothDisabled))
                return@callbackFlow
            }

        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
        val scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                if (result != null) {
                    trySend(result)
                }
            }

            override fun onScanFailed(errorCode: Int) {
                close(BleException(BleError.ScanFailed))
            }
        }
        scanner.startScan(null, scanSettings, scanCallback)

        awaitClose {
            scanner.stopScan(scanCallback)
        }
    }
}