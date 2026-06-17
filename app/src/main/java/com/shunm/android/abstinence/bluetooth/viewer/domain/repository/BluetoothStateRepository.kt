package com.shunm.android.abstinence.bluetooth.viewer.domain.repository

import com.shunm.android.abstinence.bluetooth.viewer.domain.model.BluetoothState
import kotlinx.coroutines.flow.Flow

interface BluetoothStateRepository {
    fun isBluetoothSupported() : Boolean

    fun observeState() : Flow<BluetoothState>
}