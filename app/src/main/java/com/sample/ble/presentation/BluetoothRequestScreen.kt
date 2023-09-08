package com.sample.ble.presentation

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.sample.ble.MainActivity
import com.sample.ble.util.startBleScan
import com.sample.ble.util.stopBleScan

@Composable
fun BluetoothRequestScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current as MainActivity
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
}



