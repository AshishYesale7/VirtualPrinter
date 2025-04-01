package com.virtualprinter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.virtualprinter.ui.theme.VirtualPrinterTheme

class MainActivity : ComponentActivity() {
    private lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkService = NetworkService(this)

        setContent {
            VirtualPrinterTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    VirtualPrinterScreen(
                        onStartBroadcast = { printerName ->
                            networkService.startBroadcast(printerName)
                        },
                        onStopBroadcast = {
                            networkService.stopBroadcast()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun VirtualPrinterScreen(onStartBroadcast: (String) -> Unit, onStopBroadcast: () -> Unit) {
    var printerName by remember { mutableStateOf("Virtual Printer") }
    var status by remember { mutableStateOf("Idle") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Virtual Printer", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = printerName,
            onValueChange = { printerName = it },
            label = { Text("Printer Name") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Button(onClick = {
                onStartBroadcast(printerName)
                status = "Broadcasting as: $printerName"
            }) {
                Text("Start Virtual Printer")
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(onClick = {
                onStopBroadcast()
                status = "Stopped"
            }) {
                Text("Stop Virtual Printer")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Status: $status")
    }
}
