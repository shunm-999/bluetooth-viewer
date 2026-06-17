package com.shunm.android.abstinence.bluetooth.viewer.data.bluetooth.state

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.shunm.android.abstinence.bluetooth.viewer.data.bluetooth.state.BluetoothStateReceiver.States.State
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BluetoothStateReceiver {

    data class States(
        val previous: State,
        val current: State
    ) {
        enum class State {
            Off,
            TurningOn,
            On,
            TurningOff
        }
    }

    fun state(context: Context): Flow<States> = callbackFlow {
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
                    States(
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

    private fun Int.toBluetoothState(): State? {
        return when (this) {
            BluetoothAdapter.STATE_OFF -> State.Off
            BluetoothAdapter.STATE_TURNING_ON -> State.TurningOn
            BluetoothAdapter.STATE_ON -> State.On
            BluetoothAdapter.STATE_TURNING_OFF -> State.TurningOff
            else -> null
        }
    }
}