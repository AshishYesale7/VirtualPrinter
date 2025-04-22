package com.virtualprinter.server

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.IHTTPSession

/**
 * A simple IPP-stub server on port 3000,
 * logs every request into [logs].
 */
object VirtualPrintService {
    private const val TAG = "VirtualPrintService"
    const val REAL_PORT = 3000

    // Compose-friendly observable log list:
    val logs = mutableStateListOf<String>()
    private var server: NanoHTTPD? = null

    fun start() {
        if (server != null) return
        logs += "ACTION START IPP VIRTUAL PRINTER"

        server = object : NanoHTTPD(REAL_PORT) {
            override fun serve(session: IHTTPSession): Response {
                val method = session.method
                val uri = session.uri
                val from = session.headers["remote-addr"] ?: "unknown"
                logs += "req:$method $uri  from:$from"

                // Detect operation from URI
                when {
                    uri.endsWith("/validate")   -> logs += "oper:Validate-Job"
                    uri.endsWith("/attributes") -> logs += "oper:Get-Printer-Attributes"
                    uri.endsWith("/print")      -> {
                        logs += "oper:Print-Job"
                        logs += "Received print job, skipping save due to read-only FS"
                    }
                    uri.endsWith("/jobs")       -> logs += "oper:Get-Jobs"
                }

                // Read request content (but don’t save it)
                val contentLength = session.headers["content-length"]?.toIntOrNull() ?: 0
                if (contentLength > 0) {
                    val buffer = ByteArray(contentLength)
                    session.inputStream.read(buffer)
                    logs += "[${System.currentTimeMillis()}] Received IPP content: ${buffer.size} bytes"
                }

                // Respond with minimal valid IPP response
                val ippResponse = byteArrayOf(
                    0x01, 0x01,             // IPP version 1.1
                    0x00, 0x00,             // Status: successful-ok
                    0x00, 0x00, 0x00, 0x01, // Request ID: 1
                    0x03                    // end-of-attributes-tag
                )

                return newFixedLengthResponse(
                    Response.Status.OK,
                    "application/ipp",
                    ippResponse.inputStream(),
                    ippResponse.size.toLong()
                )
            }
        }

        try {
            server!!.start()
            Log.d(TAG, "Server started on port $REAL_PORT")
        } catch (e: Exception) {
            logs += "ERROR starting server: ${e.message}"
            Log.e(TAG, "Server start error", e)
        }
    }

    fun stop() {
        server?.stop()
        server = null
        logs += "ACTION STOP IPP VIRTUAL PRINTER"
        Log.d(TAG, "Server stopped")
    }

    fun clearLogs() = logs.clear()
}
