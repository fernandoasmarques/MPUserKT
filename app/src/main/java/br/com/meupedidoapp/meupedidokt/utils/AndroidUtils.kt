package br.com.meupedidoapp.meupedidokt.utils

import android.content.Context
import android.net.ConnectivityManager

object AndroidUtils {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networks = connectivity.allNetworks
        return networks
                .map {connectivity.getNetworkInfo(it)}
                .any {it.isConnected}
    }
}