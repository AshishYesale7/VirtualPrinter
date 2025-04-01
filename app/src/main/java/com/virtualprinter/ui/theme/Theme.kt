package com.virtualprinter.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun VirtualPrinterTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(), // or darkColorScheme()
        typography = Typography(),
        content = content
    )
}
