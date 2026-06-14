package com.shunm.android.abstinence.bluetooth.viewer.scanner

import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class BluetoothPermissionRequestContract : ActivityResultContract<Unit, Boolean>() {
    override fun createIntent(
        context: Context,
        input: Unit
    ): Intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ): Boolean {
        return resultCode == RESULT_OK
    }
}