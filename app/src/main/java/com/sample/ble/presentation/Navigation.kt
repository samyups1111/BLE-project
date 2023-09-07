package com.sample.ble.presentation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.BluetoothRequest.route,
    ) {
    composable(Screen.StartScreen.route) {
        StartScreen(
            navController = navController,
        )
    }
    composable(Screen.TemperatureHumidityScreen.route) {
        TemperatureHumidityScreen()
    }
    composable(Screen.BluetoothRequest.route) {
        BluetoothRequestScreen()
    }
}

}

sealed class Screen(val route: String) {
    object StartScreen: Screen("start_screen")
    object TemperatureHumidityScreen: Screen("temp_humid_screen")
    object BluetoothRequest: Screen("bluetooth_request")
}