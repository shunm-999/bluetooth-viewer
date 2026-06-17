package com.shunm.android.abstinence.bluetooth.viewer.data.bluetooth.state

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BluetoothStateReceiver {

    data class State(
        val previous: BluetoothState,
        val current: BluetoothState
    )

    fun state(context: Context): Flow<State> = callbackFlow {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(
                context: Context?,
                intent: Intent?
            ) {
                if (context == null || intent == null) {
                    return
                }
                if (intent.action != BluetoothAdapter.ACTION_STATE_CHANGED) {
                    return
                }
                val previousState =
                    intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, -1).toBluetoothState()
                        ?: return
                val currentState =
                    intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1).toBluetoothState()
                        ?: return

                trySend(
                    State(
                        previous = previousState,
                        current = currentState
                    )
                )
            }
        }

        context.registerReceiver(receiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))

        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }

    private fun Int.toBluetoothState(): BluetoothState? {
        return when (this) {
            BluetoothAdapter.STATE_OFF -> BluetoothState.Off
            BluetoothAdapter.STATE_TURNING_ON -> BluetoothState.TurningOn
            BluetoothAdapter.STATE_ON -> BluetoothState.On
            BluetoothAdapter.STATE_TURNING_OFF -> BluetoothState.TurningOff
            else -> null
        }
    }
}