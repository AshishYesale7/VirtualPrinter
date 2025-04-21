package com.virtualprinter

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log

// NetworkService class is responsible for handling the Network Service Discovery (NSD) functionality
// It registers and unregisters a virtual printer service for discovery by other devices.
class NetworkService(private val context: Context) {
    private val TAG = "NetworkService"  // Used for logging
    private val SERVICE_TYPE = "_ipp._tcp."  // The service type to advertise for IPP (Internet Printing Protocol) over TCP.

    // Advertised port (631 is typically used for IPP, but we use 3000 for actual traffic)
    companion object {
        const val ADVERTISED_PORT = 3000
    }

    // NsdManager instance to manage NSD operations
    private var nsdManager: NsdManager? = null
    // RegistrationListener to handle the service registration lifecycle events
    private var registrationListener: NsdManager.RegistrationListener? = null

    // Starts broadcasting the service to be discovered on the network
    fun startBroadcast(serviceName: String) {
        // Initialize the NsdManager to start managing NSD operations
        nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager

        // Create an instance of NsdServiceInfo with service details
        val info = NsdServiceInfo().apply {
            this.serviceName = serviceName  // Set the service name
            this.serviceType = SERVICE_TYPE  // Set the service type (IPP over TCP)
            this.port = ADVERTISED_PORT  // Set the advertised port for discovery (3000 in this case)
        }

        // Create a registration listener to handle the NSD events
        registrationListener = object : NsdManager.RegistrationListener {
            // Callback triggered when the service is successfully registered
            override fun onServiceRegistered(si: NsdServiceInfo) {
                Log.d(TAG, "Registered: ${si.serviceName} on port ${si.port}")
            }

            // Callback triggered when registration fails
            override fun onRegistrationFailed(si: NsdServiceInfo, err: Int) {
                Log.e(TAG, "Registration failed: $err")
            }

            // Callback triggered when the service is unregistered
            override fun onServiceUnregistered(si: NsdServiceInfo) {
                Log.d(TAG, "Unregistered: ${si.serviceName}")
            }

            // Callback triggered when unregistration fails
            override fun onUnregistrationFailed(si: NsdServiceInfo, err: Int) {
                Log.e(TAG, "Unregistration failed: $err")
            }
        }

        // Try registering the service with the NsdManager
        try {
            nsdManager?.registerService(info, NsdManager.PROTOCOL_DNS_SD, registrationListener)
            Log.d(TAG, "Broadcast started: $serviceName ➔ port $ADVERTISED_PORT")
        } catch (e: Exception) {
            Log.e(TAG, "NSD error: ${e.message}")
        }
    }

    // Stops broadcasting the service
    fun stopBroadcast() {
        // Unregister the service using the listener if it exists
        registrationListener?.let {
            try {
                nsdManager?.unregisterService(it)
                Log.d(TAG, "Broadcast stopped")
            } catch (e: Exception) {
                Log.e(TAG, "NSD stop error: ${e.message}")
            }
        }
    }
}
