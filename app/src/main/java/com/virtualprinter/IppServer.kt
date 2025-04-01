package com.virtualprinter

import android.util.Log
import fi.iki.elonen.NanoHTTPD

class IppServer(port: Int) : NanoHTTPD(port) {
    override fun serve(session: IHTTPSession?): Response {
        Log.d("IppServer", "Received IPP request: ${session?.uri}")

        // Respond with a basic message (IPP usually responds with binary data)
        val message = "Virtual Printer Response"
        return newFixedLengthResponse(Response.Status.OK, "text/plain", message)
    }
}