package com.unibo.unievents.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

enum class NetworkState {
    Available,
    Unavailable,
    Losing,
    Lost
}

class NetworkObserver(ctx: Context) {
    private val connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val status: Flow<NetworkState> = callbackFlow {
        Log.d("NetworkObserver", "Callback started")

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(NetworkState.Available)
                Log.d("NetworkObserver", "Connection available")
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend(NetworkState.Unavailable)
                Log.d("NetworkObserver", "Connection unavailable")
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                trySend(NetworkState.Losing)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(NetworkState.Lost)
                Log.d("NetworkObserver", "Connection lost")
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)
        val currentNetwork = connectivityManager.activeNetwork ?: trySend(NetworkState.Unavailable)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}
