package com.sample.ble

import android.Manifest
import android.bluetooth.BluetoothSocket
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.OutputStream
import java.util.UUID
import javax.inject.Inject

private const val TAG = "MY_APP_DEBUG_TAG"

// Defines several constants used when transmitting messages between the
// service and the UI.
const val MESSAGE_READ: Int = 0
const val MESSAGE_WRITE: Int = 1
const val MESSAGE_TOAST: Int = 2
// ... (Add other message types here as needed.)

class BluetoothService @AssistedInject constructor(
    private val handler: Handler,
    @ApplicationContext val context: Context,
    @Assisted val result: ScanResult,
) {


//    val permission = ActivityCompat.checkSelfPermission(
//    context,
//    Manifest.permission.BLUETOOTH_CONNECT
//    ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.S
//    val socket = permission ? result.device.createRfcommSocketToServiceRecord(UUID.fromString(R.string.uuid.toString())) : null

//    if (ActivityCompat.checkSelfPermission(
//    this,
//    Manifest.permission.BLUETOOTH_CONNECT
//    ) == PackageManager.PERMISSION_GRANTED
//
////    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {
//
//    if (ActivityCompat.checkSelfPermission(
//            this,
//            Manifest.permission.BLUETOOTH_CONNECT
//        ) == PackageManager.PERMISSION_GRANTED
//    ) {
//        val socket: BluetoothSocket = result.device.createRfcommSocketToServiceRecord(UUID.fromString(R.string.uuid.toString()))
//    }
//
//    val outStream: OutputStream? = socket?.outputStream
//    val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream


    // Call this from the main activity to send data to the remote device.
//    fun setUpSocketAndWriteToStream(bytes: ByteArray) {
//        val permission = ActivityCompat.checkSelfPermission(
//            context,
//            Manifest.permission.BLUETOOTH_CONNECT
//        ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.S
//        val socket = if (permission) result.device.createRfcommSocketToServiceRecord(UUID.fromString(context.resources.getString(R.string.uuid))) else null
//        Log.d("andrea", "socket? ${socket!=null}")
//        socket?.connect()
//        val outStream: OutputStream? = socket?.outputStream
//        val mmBuffer = ByteArray(1024) // mmBuffer store for the stream
//        val bytes_to_send = 0x01
//        try {
//            outStream?.write(bytes_to_send)
//        } catch (e: IOException) {
//            Log.e(TAG, "Error occurred when sending data", e)
//
//            // Send a failure message back to the activity.
//            val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
//            val bundle = Bundle().apply {
//                putString("toast", "Couldn't send data to the other device")
//            }
//            writeErrorMsg.data = bundle
//            handler.sendMessage(writeErrorMsg)
//            return
//        }
//
//        // Share the sent message with the UI activity.
//        val writtenMsg = handler.obtainMessage(
//            MESSAGE_WRITE, -1, -1, mmBuffer)
//        writtenMsg.sendToTarget()
//        socket?.close()
//    }
}