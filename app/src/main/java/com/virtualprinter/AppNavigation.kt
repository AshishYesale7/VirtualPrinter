package com.virtualprinter

// Compose UI + Navigation imports
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*

// Screens for navigation
import com.virtualprinter.ui.screens.DebugScreen
import com.virtualprinter.ui.screens.HomeScreen
import com.virtualprinter.ui.screens.PrintScreen

// Composable that sets up bottom navigation and screen routing
@Composable
fun AppNavigation(viewModel: PrinterViewModel, networkService: NetworkService) {
    val navController = rememberNavController() // Remember navigation controller

    Scaffold(
        // Bottom navigation bar with 3 items
        bottomBar = {
            NavigationBar {
                listOf("home" to "Home", "print" to "Print", "debug" to "Debug").forEach { (route, label) ->
                    NavigationBarItem(
                        selected = navController.currentDestination?.route == route, // Highlight selected tab
                        onClick = { navController.navigate(route) }, // Navigate to destination
                        label = { Text(label) }, // Display tab label
                        icon = { Icon(Icons.Default.Print, contentDescription = null) } // Same icon for simplicity
                    )
                }
            }
        }
    ) { padding ->
        // Navigation host that defines what screen to show for each route
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding) // Apply scaffold padding
        ) {
            composable("home") { HomeScreen(viewModel) }               // Route: Home tab
            composable("print") { PrintScreen(viewModel, networkService) } // Route: Print tab
            composable("debug") { DebugScreen(viewModel) }             // Route: Debug tab
        }
    }
}
