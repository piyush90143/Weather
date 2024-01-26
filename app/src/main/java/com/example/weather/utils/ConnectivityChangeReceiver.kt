package com.example.weather.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

class ConnectivityChangeReceiver : BroadcastReceiver() {


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant")
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val connMgr = it
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connMgr.activeNetwork
            var canReachInternet = false
            if (connMgr.getNetworkCapabilities(network)?.hasCapability(NetworkCapabilities.TRANSPORT_CELLULAR) == true
                || connMgr.getNetworkCapabilities(network)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
            ) {
                canReachInternet = true
            }

            if (canReachInternet.not()) {
                showAlertDialog(
                    it, "Internet Connection Lost",
                    "Please retry with a stable connection"
                )
            }
        }

    }
}