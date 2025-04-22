package com.virtualprinter

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.virtualprinter.ui.screens.DebugScreen
import com.virtualprinter.ui.screens.HomeScreen
import com.virtualprinter.ui.screens.PrintScreen

@Composable
fun AppNavigation(viewModel: PrinterViewModel, networkService: NetworkService) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf("home" to "Home", "print" to "Print", "debug" to "Debug").forEach { (route, label) ->
                    NavigationBarItem(
                        selected = navController.currentDestination?.route == route,
                        onClick = { navController.navigate(route) },
                        label = { Text(label) },
                        icon = { Icon(Icons.Default.Print, contentDescription = null) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(navController, startDestination = "home", Modifier.padding(padding)) {
            composable("home") { HomeScreen(viewModel) }
            composable("print") { PrintScreen(viewModel, networkService) }
            composable("debug") { DebugScreen(viewModel) }
        }
    }
}
