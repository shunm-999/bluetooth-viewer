package com.shunm.android.abstinence.bluetooth.viewer.presentation.scan.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.shunm.android.abstinence.bluetooth.viewer.domain.model.BleDevice
import com.shunm.android.abstinence.bluetooth.viewer.domain.model.BleScanResult

@Composable
fun DeviceListItem(
    bleScanResult: BleScanResult,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        val bleDevice = bleScanResult.bleDevice
        when (val name = bleDevice.name) {
            is BleDevice.Name.Present -> {
                Text(
                    text = name.value,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            is BleDevice.Name.None -> {
                Text(
                    text = "No Name",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        Text(
            text = bleDevice.address,
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = bleScanResult.rssi.toString(),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeviceListItemPreview_Present() {
    MaterialTheme {
        DeviceListItem(
            bleScanResult = BleScanResult(
                bleDevice = BleDevice(
                    name = BleDevice.Name.Present(
                        value = "BleDevice"
                    ),
                    address = "AA:BB:CC:DD:EE:FF"
                ),
                rssi = -120
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeviceListItemPreview_None() {
    MaterialTheme {
        DeviceListItem(
            bleScanResult = BleScanResult(
                bleDevice = BleDevice(
                    name = BleDevice.Name.None,
                    address = "AA:BB:CC:DD:EE:FF"
                ),
                rssi = -60
            ),
        )
    }
}