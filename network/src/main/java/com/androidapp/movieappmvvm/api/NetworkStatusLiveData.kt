package com.androidapp.movieappmvvm.api

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import com.androidapp.movieappmvvm.di.AppScope
import javax.inject.Inject

@AppScope
class NetworkStatusLiveData @Inject constructor(context: Context) : LiveData<Boolean>() {

    private val availableNetworks = mutableSetOf<Network>()
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val request: NetworkRequest = NetworkRequest.Builder().build()

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            availableNetworks.remove(network)
            update(availableNetworks.isNotEmpty())
        }

        override fun onAvailable(network: Network) {
            availableNetworks.add(network)
            update(availableNetworks.isNotEmpty())
        }
    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        connectivityManager.registerNetworkCallback(request, callback)
    }

    override fun onInactive() {
        connectivityManager.unregisterNetworkCallback(callback)
    }

    @SuppressLint("MissingPermission")
    fun isNetworkAvailable() = connectivityManager.allNetworks.isNotEmpty()

    private fun update(online: Boolean) {
        if (online != value) {
            postValue(online)
        }
    }
}