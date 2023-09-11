package com.sample.ble.presentation

import android.bluetooth.le.ScanResult
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sample.ble.MainActivity
import com.sample.ble.util.startBleScan
import com.sample.ble.util.stopBleScan

@Composable
fun BluetoothRequestScreen(
    vm: BluetoothRequestScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current as MainActivity
    val scanResults = vm.scanResultsFlow.collectAsState()
    Log.d("sammy", "bluetoothRequest ")
    if (scanResults.value is UiState.Success) {
        Log.d("sammy", "size in composable = ${(scanResults.value as UiState.Success).list}. id = ${scanResults.value.hashCode()}")
    }
    Button(
        onClick = {
            if (context.isScanning) {
                context.stopBleScan()
            } else {
                context.startBleScan()
            }

        }) {
        Text(
            text = "Start bluetooth scan"
        )
    }
    if (scanResults.value is UiState.Success) {
        ScanResult((scanResults.value as UiState.Success).list)
    }
    MockList(devices = listOf("sdf", "sdf", "sldjfl"))
}

@Composable
private fun MockList(
    devices: List<String>
) {
    LazyColumn {
        items(devices) { result ->
            Row() {
                Text(
                    //text = result.device.name ?: "Unnamed",
                    text = "placeholder"
                )
                Text(
                    text = result,
                )
                Text(
                    text = "wlja")
            }

        }
    }
}

@Composable
private fun ScanResult(
    devices: List<ScanResult>,
    modifier: Modifier = Modifier,
    onClick: (device: ScanResult) -> Unit = {},
) {
    Log.d("sammy", "list =${devices.size}")
    LazyColumn {
        items(devices) { result ->
            Row(
                Modifier.clickable {
                    onClick
                }
            ) {
                Text(
                    //text = result.device.name ?: "Unnamed",
                    text = "placeholder"
                    )
                Text(
                    text = result.device.address,
                    )
                Text(
                    text = "${result.rssi} dBm")
            }

        }
    }
}



