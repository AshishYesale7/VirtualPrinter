package com.virtualprinter.ui.screens

// Required Compose and UI imports
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// ViewModel to share state across screens
import com.virtualprinter.PrinterViewModel

// Virtual IPP server that provides the log list
import com.virtualprinter.server.VirtualPrintService

// Debug tab Composable to display logs and control actions
@Composable
fun DebugScreen(viewModel: PrinterViewModel) {
    // Observe logs from the virtual IPP server (Compose-friendly list)
    val logs = VirtualPrintService.logs

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Section title
        Text("Debug Logs", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(10.dp))

        // LazyColumn to display log messages
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(logs) { log ->
                Text(log)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Button to clear the logs from the service
        Button(onClick = {
            VirtualPrintService.clearLogs()
        }) {
            Text("Clear Logs")
        }
    }
}
