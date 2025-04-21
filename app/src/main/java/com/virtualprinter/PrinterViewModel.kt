package com.virtualprinter

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

// ViewModel to manage printer status and name, making them observable by the UI
class PrinterViewModel : ViewModel() {
    // Mutable state for printer status (e.g., "Idle", "Printing")
    val status = mutableStateOf("Idle")

    // Mutable state for printer name
    var printerName = mutableStateOf("Virtual Printer")

    // Updates the printer status
    fun updateStatus(newStatus: String) {
        status.value = newStatus
    }
}
