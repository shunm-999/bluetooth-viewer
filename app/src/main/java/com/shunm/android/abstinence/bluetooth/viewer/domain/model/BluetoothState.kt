package com.shunm.android.abstinence.bluetooth.viewer.domain.model

sealed interface BluetoothState {
    data object Off : BluetoothState
    data object TurningOn : BluetoothState
    data object On : BluetoothState
    data object TurningOff : BluetoothState
}