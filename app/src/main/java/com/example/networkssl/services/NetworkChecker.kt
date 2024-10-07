package com.example.networkssl.services

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkChecker(var connectivityManager: ConnectivityManager) {

    fun performAction(action: () -> Unit) {
        if (hasValidInternetConnection()) {
            action()
        }
    }

    private fun hasValidInternetConnection(): Boolean {
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        return networkCapabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    }

}