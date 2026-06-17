package com.shunm.android.abstinence.bluetooth.viewer.data.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.shunm.android.abstinence.bluetooth.viewer.domain.None
import com.shunm.android.abstinence.bluetooth.viewer.domain.Option
import com.shunm.android.abstinence.bluetooth.viewer.domain.Some

class BluetoothAdapterProvider {

    fun getAdapter(context: Context): Option<BluetoothAdapter> {
        val bluetoothManager: BluetoothManager =
            context.getSystemService(BluetoothManager::class.java) ?: return None
        val bluetoothAdapter = bluetoothManager.adapter

        return if (bluetoothAdapter != null) {
            Some(bluetoothAdapter)
        } else {
            None
        }
    }
}