package com.shunm.android.abstinence.bluetooth.viewer.presentation.scan

import com.shunm.android.abstinence.bluetooth.viewer.domain.model.BleError
import com.shunm.android.abstinence.bluetooth.viewer.domain.model.BleScanResult

sealed interface ScanUiState {
    data object Idle : ScanUiState
    data class Scanning(
        val devices: List<BleScanResult>
    ) : ScanUiState {
        fun add(device: BleScanResult): Scanning {
            return this.copy(
                devices = this.devices + device
            )
        }
    }

    data class Error(
        val error: BleError
    ) : ScanUiState
}