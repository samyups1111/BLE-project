package com.sample.ble.presentation

import android.Manifest
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BluetoothRequestScreenViewModel @Inject constructor(): ViewModel() {

//    private val _scanResultsFlow = MutableStateFlow<MutableList<ScanResult>>(mutableListOf())
//    val scanResultsFlow = _scanResultsFlow.asStateFlow()

    private val _scanResultsFlow = MutableStateFlow(BluetoothListState())
    val scanResultsFlow = _scanResultsFlow.asStateFlow()

    fun setScanResults(newDevice: ScanResult?) {
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

        _scanResultsFlow.update { currentState ->
            currentState.scanResults.add(newDevice!!)
            currentState.copy(
                scanResults = currentState.scanResults
            )

        }
    }
}



data class BluetoothListState(
    val scanResults: MutableList<ScanResult> = mutableListOf()
)
