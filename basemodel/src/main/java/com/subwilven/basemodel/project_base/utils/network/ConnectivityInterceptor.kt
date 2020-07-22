package com.subwilven.basemodel.project_base.utils.network

import com.subwilven.basemodel.project_base.BaseApplication
import com.subwilven.basemodel.project_base.utils.NetworkManager

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Response

public class ConnectivityInterceptor : Interceptor {

    @Throws(NoConnectivityException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!NetworkManager.isNetworkConnected(BaseApplication.instance!!)) {
            throw NoConnectivityException()
        } else {
            chain.proceed(chain.request())
        }
    }


    class NoConnectivityException : IOException()
}
