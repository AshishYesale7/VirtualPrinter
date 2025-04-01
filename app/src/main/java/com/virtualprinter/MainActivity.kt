package com.virtualprinter

import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.virtualprinter.ui.theme.VirtualPrinterTheme

class MainActivity : ComponentActivity() {
    private var ippServer: IppServer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VirtualPrinterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VirtualPrinterScreen(::startIppServer, ::stopIppServer)
                }
            }
        }
    }

    private fun startIppServer(port: Int = 3000) {
        try {
            ippServer = IppServer(port)
            ippServer?.start()
            Log.d("VirtualPrinter", "IPP Server started on port $port")
        } catch (e: Exception) {
            Log.e("VirtualPrinter", "Error starting IPP server: ${e.message}")
        }
    }


    private fun stopIppServer() {
        try {
            ippServer?.stop()
            Log.d("VirtualPrinter", "IPP Server stopped")
        } catch (e: Exception) {
            Log.e("VirtualPrinter", "Error stopping IPP server: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopIppServer()
    }
}

@Composable
fun VirtualPrinterScreen(
    onStartServer: (Int) -> Unit,
    onStopServer: () -> Unit
) {
    var printerName by remember { mutableStateOf(TextFieldValue("Virtual Printer")) }
    var status by remember { mutableStateOf("Idle") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Virtual Printer", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = printerName,
            onValueChange = { printerName = it },
            label = { Text("Printer Name") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            onStartServer(3000)
            status = "Broadcasting as: ${printerName.text}"
            Log.d("VirtualPrinter", "Started broadcasting as: ${printerName.text}")
        }) {
            Text("Start Virtual Printer")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            onStopServer()
            status = "Stopped"
            Log.d("VirtualPrinter", "Stopped broadcasting")
        }) {
            Text("Stop Virtual Printer")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Status: $status")
    }
}
