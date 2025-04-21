package com.virtualprinter.server

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.IHTTPSession


/**
 * A simple IPP‐stub server on port 3000,
 * logs every request into [logs].
 */
object VirtualPrintService {
    private const val TAG = "VirtualPrintService"
    const val REAL_PORT = 3000

    // Compose‐friendly observable log list:
    val logs = mutableStateListOf<String>()
    private var server: NanoHTTPD? = null

    fun start() {
        if (server != null) return
        logs += "ACTION START IPP VIRTUAL PRINTER"
        server = object : NanoHTTPD(REAL_PORT) {
            override fun serve(session: IHTTPSession): Response {
                val method = session.method
                val uri    = session.uri
                val from   = session.headers["remote-addr"] ?: "unknown"
                logs += "req:$method $uri  from:$from"
                // detect operation from path or parameters:
                when {
                    uri.endsWith("/validate")   -> logs += "oper:Validate-Job"
                    uri.endsWith("/attributes") -> logs += "oper:Get-Printer-Attributes"
                    uri.endsWith("/print")      -> logs += "oper:Print-Job\nprint task received"
                    uri.endsWith("/jobs")       -> logs += "oper:Get-Jobs"
                }
                // stub reply:
                return newFixedLengthResponse(
                    Response.Status.OK,
                    "application/ipp",
                    "OK"
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
