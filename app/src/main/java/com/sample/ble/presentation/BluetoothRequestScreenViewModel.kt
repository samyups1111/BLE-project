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

//    private val _scanResultsFlow = MutableStateFlow<MutableList<ScanResult>>(mutableListOf())
//    val scanResultsFlow = _scanResultsFlow.asStateFlow()

    private val _scanResultsFlow = MutableStateFlow<UiState>(UiState.Loading)
    val scanResultsFlow = _scanResultsFlow.asStateFlow()
    private val allResults = mutableListOf<ScanResult>()
    //private val bluetoothService
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
    private fun setupService(result: ScanResult): BluetoothService {
        return bluetoothServiceFactory.create(result)
    }


    fun addScanResults(newDevice: ScanResult?) {
//        val scanListCopy = mutableListOf<ScanResult>()
//        //scanListCopy.addAll(_scanResultsFlow.value)
//        Log.d("sammy", "newDevice size = ${scanListCopy.size}")
//
//        if (newDevice != null) {
//            Log.d("sammy", "newDevice address = ${newDevice.device.address}")
//            _scanResultsFlow.value.add(newDevice)
//            Log.d("sammy", "flow size = ${_scanResultsFlow.value.size}")
//
//            _scanResultsFlow.value = scanListCopy
//        }

//        _scanResultsFlow.update { currentState ->
//            currentState.scanResults.add(newDevice!!)
//            currentState.copy(
//                scanResults = currentState.scanResults
//            )
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





//        try {
//            bluetoothGatt = result.device.connectGatt(context, false, bluetoothGattCallback)
//        } catch (e: SecurityException) {
//            Log.d("andrea", "security exception")
//        }
//        //
//        viewModelScope.launch(Dispatchers.IO) {
//            if (ActivityCompat.checkSelfPermission(
//                    this@BluetoothRequestScreenViewModel.context,
//                    Manifest.permission.BLUETOOTH_SCAN
//                ) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
//            ) {
//                //
//                return@launch
//            }
//            Looper.prepare()
//            adapter.cancelDiscovery()




//            setupService(result).setUpSocketAndWriteToStream(byteArrayOf())

//            socket?.let { socket ->
//                // Connect to the remote device through the socket. This call blocks
//                // until it succeeds or throws an exception.
//                socket.connect()
//
//                // The connection attempt succeeded. Perform work associated with
//                // the connection in a separate thread.
//                // PASS SOCKET HERE??
//                setupService(result).writeToStream(byteArrayOf())
//            }
//            try{
//                socket?.close()
//            } catch (e: IOException) {
//                Log.e("andrea", "couldn't close socket")
//            }

//        }
    }
}


data class BluetoothListState(
    val scanResults: MutableList<ScanResult> = mutableListOf()
)

sealed class UiState() {
    class Success(val list: List<ScanResult>): UiState()
    object Loading: UiState()
}
