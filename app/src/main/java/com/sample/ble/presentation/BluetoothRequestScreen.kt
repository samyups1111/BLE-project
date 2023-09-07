package com.sample.ble.presentation

import android.app.Activity
import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.sample.ble.util.hasRequiredRuntimePermissions
import com.sample.ble.util.requestRelevantRuntimePermissions
import com.sample.ble.util.startBleScan

@Composable
fun BluetoothRequestScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current as Activity
    Button(
        onClick = {
            startBleScan(context)
        }) {
        Text(
            text = "Start bluetooth scan"
        )
    }
}



