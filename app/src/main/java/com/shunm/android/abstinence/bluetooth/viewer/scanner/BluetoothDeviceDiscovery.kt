package com.shunm.android.abstinence.bluetooth.viewer.scanner

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext


@Composable
fun BluetoothDeviceDiscoveryEffect(
    onDiscovery: (BluetoothDevice) -> Unit
) {
    val context = LocalContext.current
    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(
                context: Context?,
                intent: Intent?
            ) {
                val device: BluetoothDevice =
                    intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) ?: return
                onDiscovery(device)
            }
        }
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)

        context.registerReceiver(receiver, filter)
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }
}