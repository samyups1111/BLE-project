package com.sample.ble.di

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
import com.sample.ble.BluetoothService
import com.sample.ble.R
import com.sample.ble.presentation.BluetoothRequestScreenViewModel
import dagger.Module
import dagger.Provides
import dagger.assisted.AssistedFactory
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.UUID
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
                with(result) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                    ) {
                        return
                    }
                    vm.addScanResults(this)

                    Log.i("ScanCallback", "Found BLE device! Name: ${this?.device?.name ?: "Unnamed"}, address: ${this?.device?.address}")
                }
            }

            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
                Log.d("andrea", "scan failed :( rip")
            }
            // to trigger this, add .setReportDelay(100) to ScanSettings @ MainActivity.startBleScan in extension.kt
            override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                super.onBatchScanResults(results)
            }
        }
        return scanCallback
    }

    @Provides
    @Singleton
    fun provideBluetoothRequestScreenViewModel(
        @ApplicationContext context: Context,
        adapter: BluetoothAdapter,
        bluetoothServiceFactory: BluetoothServiceFactory,
        ) = BluetoothRequestScreenViewModel(context, adapter, bluetoothServiceFactory)

    @AssistedFactory
    interface BluetoothServiceFactory {
        fun create(result: ScanResult): BluetoothService
    }

    @Provides
    @Singleton
    fun provideHandler() = Handler()

}