package com.subwilven.basemodel.project_base.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

public object NetworkManager {

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}
