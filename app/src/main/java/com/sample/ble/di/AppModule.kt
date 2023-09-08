package com.sample.ble.di

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.sample.ble.presentation.BluetoothRequestScreenViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBluetoothAdapter(
        @ApplicationContext context: Context,
    ): BluetoothAdapter {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return manager.adapter
    }

    @Provides
    @Singleton
    fun provideBluetoothScanner(
        adapter: BluetoothAdapter
    ) = adapter.bluetoothLeScanner

    @Provides
    @Singleton
    fun provideScanCallback(
        @ApplicationContext context: Context,
        vm: BluetoothRequestScreenViewModel,
    ): ScanCallback {
        val scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                super.onScanResult(callbackType, result)
                with(result) { // .device?
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    vm.setScanResults(this)

                    Log.i("ScanCallback", "Found BLE device! Name: ${this?.device?.name ?: "Unnamed"}, address: ${this?.device?.address}")
                }
            }
        }
        return scanCallback
    }

    @Provides
    @Singleton
    fun provideBluetoothRequestScreenViewModel() = BluetoothRequestScreenViewModel()
}