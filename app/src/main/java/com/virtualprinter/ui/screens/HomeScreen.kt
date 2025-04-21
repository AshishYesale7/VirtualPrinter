package com.virtualprinter.ui.screens

// Compose UI components for layout and design
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Shared ViewModel that holds printer state
import com.virtualprinter.PrinterViewModel

// Home screen composable, displayed when app opens or Home tab is selected
@Composable
fun HomeScreen(viewModel: PrinterViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp), // Padding around the screen
        horizontalAlignment = Alignment.CenterHorizontally, // Center items horizontally
        verticalArrangement = Arrangement.Center // Center items vertically
    ) {
        // Main title
        Text("Welcome to Virtual Printer", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

        // Display current printer status from shared ViewModel
        Text("Status: ${viewModel.status.value}")

        // Helpful message for user guidance
        Text("Use the Print tab to start your virtual IPP service and Debug tab to watch logs.")
    }
}
