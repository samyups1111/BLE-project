package com.sample.ble.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sample.ble.MainActivity
import com.sample.ble.MainActivity.Companion.RUNTIME_PERMISSION_REQUEST_CODE
import com.sample.ble.R
import java.util.UUID

fun Context.hasPermission(permissionType: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permissionType) ==
            PackageManager.PERMISSION_GRANTED
}
fun Context.hasRequiredRuntimePermissions(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        hasPermission(Manifest.permission.BLUETOOTH_SCAN) &&
                hasPermission(Manifest.permission.BLUETOOTH_CONNECT)
    } else {
        hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}

fun Activity.requestRelevantRuntimePermissions() {
    if (hasRequiredRuntimePermissions()) { return }
    when {
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S -> {
            requestLocationPermission(this)
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            requestBluetoothPermissions(this)
        }
    }
}

fun requestLocationPermission(activity: Activity) {
    activity.runOnUiThread {
        val alert = AlertDialog.Builder(activity)
        alert.apply {
            setTitle("Location permission required")
            setMessage("Starting from Android M (6.0), the system requires apps to be granted " +
                    "location access in order to scan for BLE devices.")
            //isCancelable = false
            setPositiveButton(android.R.string.ok) { _, _ ->
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MainActivity.RUNTIME_PERMISSION_REQUEST_CODE
                )
            }
        }.show()
    }
}

@RequiresApi(Build.VERSION_CODES.S)
fun requestBluetoothPermissions(activity: Activity) {
    activity.runOnUiThread {
        val alert = AlertDialog.Builder(activity)
        alert.apply {
            setTitle("Bluetooth permissions required")
            setMessage("Starting from Android 12, the system requires apps to be granted " +
                    "Bluetooth access in order to scan for and connect to BLE devices.")
            //isCancelable = false
            setPositiveButton(android.R.string.ok) { _, _ ->
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ),
                    RUNTIME_PERMISSION_REQUEST_CODE
                )
            }
        }.show()
    }
}

fun MainActivity.startBleScan() {
    Log.d("sammy", "startBleScan")

    if (!this.hasRequiredRuntimePermissions()) {
        Log.d("sammy", "does Not have required Runtime Permission")
        this.requestRelevantRuntimePermissions()
    } else {
        Log.d("sammy", "YES it has required Runtime Permission")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        ) {
            return
        }
        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)// short scan periods
            .setMatchMode(ScanSettings.MATCH_MODE_STICKY) // only nearby devices
            .build()

        // Todo: What's the Arduino UUID?
//        val filter = ScanFilter.Builder().setServiceUuid(
//            ParcelUuid.fromString("PLACEHOLDER_SERVICE_UUID.toString()")
//        ).build()
        bleScanner.startScan(null, scanSettings, scanCallback)
        isScanning = true
    }
}

fun MainActivity.stopBleScan() {
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.BLUETOOTH_SCAN
        ) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    ) {
        // TODO: replace with security exception
        return
    }
    bleScanner.stopScan(scanCallback)
    isScanning = false
}
