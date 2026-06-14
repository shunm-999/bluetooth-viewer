package com.shunm.android.abstinence.bluetooth.viewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.shunm.android.abstinence.bluetooth.viewer.scanner.BluetoothAvailability
import com.shunm.android.abstinence.bluetooth.viewer.scanner.BluetoothAvailabilityChecker
import com.shunm.android.abstinence.bluetooth.viewer.scanner.BluetoothPermissionRequestContract
import com.shunm.android.abstinence.bluetooth.viewer.ui.theme.BluetoothViewerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BluetoothViewerTheme {
                BluetoothScreen()
            }
        }
    }
}

@Composable
private fun BluetoothScreen() {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(BluetoothPermissionRequestContract()) { isEnabled ->
            println("⭐️ Bluetooth enabled: $isEnabled")
        }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CheckAvailabilityButton(
                modifier = Modifier.padding(innerPadding),
                onClick = {
                    val availability =
                        BluetoothAvailabilityChecker.getAvailability(context)
                    when (availability) {
                        BluetoothAvailability.Available,
                        BluetoothAvailability.Unavailable -> {
                            // nop
                        }

                        BluetoothAvailability.Disabled -> {
                            launcher.launch(Unit)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BluetoothViewerTheme {
        Greeting("Android")
    }
}

@Composable
fun CheckAvailabilityButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = "Check Bluetooth Availability")
    }
}