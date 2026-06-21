package com.shunm.android.abstinence.bluetooth.viewer.presentation.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.shunm.android.abstinence.bluetooth.viewer.data.bluetooth.permission.BluetoothPermission

@Composable
fun BluetoothPermissionRequester(
    permissions: List<BluetoothPermission>,
    grantedContent: @Composable () -> Unit,
    deniedContent: @Composable (requestPermissions: () -> Unit) -> Unit,
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val permissionStrings = remember(permissions) {
        permissions.map { it.value }.toTypedArray()
    }

    var isPermissionGranted by remember(permissions) {
        mutableStateOf(areAllGranted(context, permissions))
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { result ->
            isPermissionGranted = result.values.all { it }
        }
    )

    val requestPermissions: () -> Unit = remember(launcher, permissionStrings) {
        { launcher.launch(permissionStrings) }
    }

    DisposableEffect(lifecycle, permissions) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                isPermissionGranted = areAllGranted(context, permissions)
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(permissions) {
        if (!areAllGranted(context, permissions)) {
            requestPermissions()
        }
    }

    if (isPermissionGranted) {
        grantedContent()
    } else {
        deniedContent(requestPermissions)
    }
}

private fun areAllGranted(
    context: Context,
    permissions: List<BluetoothPermission>,
): Boolean {
    return permissions.all { permission ->
        ContextCompat.checkSelfPermission(context, permission.value) ==
                PackageManager.PERMISSION_GRANTED
    }
}
