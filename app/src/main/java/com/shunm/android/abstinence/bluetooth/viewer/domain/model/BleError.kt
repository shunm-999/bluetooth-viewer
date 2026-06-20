package com.shunm.android.abstinence.bluetooth.viewer.domain.model

sealed interface BleError {
    data object PermissionDenied : BleError
    data object BluetoothDisabled : BleError
    data object ScanFailed : BleError
}