package com.sample.ble.presentation

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.ble.BluetoothService
import com.sample.ble.di.AppModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BluetoothRequestScreenViewModel @Inject constructor(
    val context: Context,
    private val adapter: BluetoothAdapter,
    private val bluetoothServiceFactory: AppModule.BluetoothServiceFactory,
): ViewModel() {

    private val _scanResultsFlow = MutableStateFlow<UiState>(UiState.Loading)
    val scanResultsFlow = _scanResultsFlow.asStateFlow()
    private val allResults = mutableListOf<ScanResult>()
    var bluetoothGatt: BluetoothGatt? = null

    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            val deviceAddress = gatt?.device?.address
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.w("andrea", "connected to $deviceAddress")
                    bluetoothGatt = gatt
                    // successfully connected to the GATT Server
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.w("andrea", "disconnected from $deviceAddress")
                    // disconnected from the GATT Server
                    try {
                        gatt?.close()
                    } catch (e: SecurityException) {
                        Log.w("andrea", "SecurityException on gatt.close()")
                    }
                }
            } else {
                Log.w("andrea", "Error $status encountered for $deviceAddress")
                try {
                    gatt?.close()
                } catch (e: SecurityException) {
                    Log.w("andrea", "SecurityException on gatt.close()")
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
        }
    }


    fun addScanResults(newDevice: ScanResult?) {
        if (_scanResultsFlow.value is UiState.Success) {
            Log.d("sammy", "flow size = ${(_scanResultsFlow.value as UiState.Success).list.size}")

        }
        try {
            if (newDevice?.device?.name == "ARDUINO") {
                allResults.add(newDevice)
            }
        } catch (e: SecurityException) {
            Log.d("andrea", "security exception")
        }

        _scanResultsFlow.value = UiState.Success((allResults as List<ScanResult>))

    }

    fun clearScanResults() {
        allResults.clear()
    }

    fun connectGatt(result: ScanResult, _context: Context) {
        try {
            result.device.connectGatt(_context, false, bluetoothGattCallback)
        } catch (e: SecurityException) {
            Log.e("andrea", "SecurityException at connectGatt")
        }
    }
}


data class BluetoothListState(
    val scanResults: MutableList<ScanResult> = mutableListOf()
)

sealed class UiState() {
    class Success(val list: List<ScanResult>): UiState()
    object Loading: UiState()
}
