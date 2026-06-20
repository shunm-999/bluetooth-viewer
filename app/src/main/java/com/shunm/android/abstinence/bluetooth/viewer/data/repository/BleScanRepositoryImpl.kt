package com.shunm.android.abstinence.bluetooth.viewer.data.repository

import android.Manifest
import android.bluetooth.le.ScanRecord
import android.bluetooth.le.ScanResult
import androidx.annotation.RequiresPermission
import com.shunm.android.abstinence.bluetooth.viewer.data.bluetooth.scanner.BleScanner
import com.shunm.android.abstinence.bluetooth.viewer.domain.model.BleDevice
import com.shunm.android.abstinence.bluetooth.viewer.domain.model.BleScanResult
import com.shunm.android.abstinence.bluetooth.viewer.domain.repository.BleScanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BleScanRepositoryImpl(
    private val scanner: BleScanner,
) : BleScanRepository {

    @RequiresPermission(
        allOf = [
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        ]
    )
    override fun observeResults(): Flow<BleScanResult> {
        return scanner.scan().map { result ->
            result.toBleScanResult()
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun ScanResult.toBleScanResult(): BleScanResult {
        val name = this.device.name
        return BleScanResult(
            bleDevice = BleDevice(
                name = if (name != null) {
                    BleDevice.Name.Present(name)
                } else {
                    BleDevice.Name.None
                },
                address = this.device.address
            ),
            rssi = this.rssi
        )
    }
}