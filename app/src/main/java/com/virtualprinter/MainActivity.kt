package com.virtualprinter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.virtualprinter.ui.theme.VirtualPrinterTheme

// Entry point of the Android application
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize NetworkService to handle NSD (Network Service Discovery)
        val networkService = NetworkService(this)

        // Set up Jetpack Compose content
        setContent {
            // Create or retrieve the shared ViewModel instance
            val printerViewModel: PrinterViewModel = viewModel()

            // Apply custom Material theme
            VirtualPrinterTheme {
                // Launch the composable-based navigation
                AppNavigation(printerViewModel, networkService)
            }
        }
    }
}
