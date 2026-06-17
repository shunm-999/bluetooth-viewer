package com.shunm.android.abstinence.bluetooth.viewer.data.repository

import android.content.Context
import com.shunm.android.abstinence.bluetooth.viewer.data.bluetooth.BluetoothAdapterProvider
import com.shunm.android.abstinence.bluetooth.viewer.data.bluetooth.state.BluetoothStateReceiver
import com.shunm.android.abstinence.bluetooth.viewer.domain.model.BluetoothState
import com.shunm.android.abstinence.bluetooth.viewer.domain.repository.BluetoothStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BluetoothStateRepositoryImpl(
    private val context: Context,
    private val bluetoothAdapterProvider: BluetoothAdapterProvider,
    private val bluetoothStateReceiver: BluetoothStateReceiver,
) : BluetoothStateRepository {
    override fun isBluetoothSupported(): Boolean {
        return bluetoothAdapterProvider.getAdapter(context).isSome()
    }

    override fun observeState(): Flow<BluetoothState> {
        return bluetoothStateReceiver.state(context).map { (_, current) ->
            when (current) {
                BluetoothStateReceiver.States.State.Off -> BluetoothState.Off
                BluetoothStateReceiver.States.State.TurningOn -> BluetoothState.TurningOn
                BluetoothStateReceiver.States.State.On -> BluetoothState.On
                BluetoothStateReceiver.States.State.TurningOff -> BluetoothState.TurningOff
            }
        }
    }
}