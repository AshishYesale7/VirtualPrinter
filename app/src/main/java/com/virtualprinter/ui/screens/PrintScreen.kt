package com.virtualprinter.ui.screens

// Compose UI components and state utilities
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// App-specific components
import com.virtualprinter.NetworkService
import com.virtualprinter.PrinterViewModel
import com.virtualprinter.server.VirtualPrintService

// PrintScreen Composable – allows user to start/stop virtual IPP printer and see status
@Composable
fun PrintScreen(viewModel: PrinterViewModel, networkService: NetworkService) {
    // Observe the printer name state from ViewModel
    val printerName by remember { viewModel.printerName }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center, // Center content vertically
        horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
    ) {
        // Input field to edit printer name
        OutlinedTextField(
            value = printerName,
            onValueChange = { viewModel.printerName.value = it },
            label = { Text("Printer Name") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Start and Stop buttons to control printer service and network broadcast
        Row {
            Button(onClick = {
                // Start backend IPP server and broadcast printer via NSD
                VirtualPrintService.start()
                networkService.startBroadcast(printerName)
                viewModel.updateStatus("Started on port 3000")
            }) {
                Text("Start Printer")
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(onClick = {
                // Stop IPP server and stop NSD broadcasting
                VirtualPrintService.stop()
                networkService.stopBroadcast()
                viewModel.updateStatus("Stopped")
            }) {
                Text("Stop Printer")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Display dynamic printer status from ViewModel
        Text("Status: ${viewModel.status.value}")
    }
}
