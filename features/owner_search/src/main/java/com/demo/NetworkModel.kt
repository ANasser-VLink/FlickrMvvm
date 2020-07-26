package com.demo

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.subwilven.basemodel.project_base.utils.network.ConnectivityInterceptor
import com.subwilven.basemodel.project_base.utils.network.NetworkModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModel {

    var retrofit: Retrofit? = null

    inline fun <reified T : Any> getService() :T {
        return getRetrofitObject()!!.create(T::class.java)
    }

    private fun getGson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    private fun getInterceptor(): Interceptor {
        return ConnectivityInterceptor()
    }


    private fun getOkhttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.readTimeout(7, TimeUnit.SECONDS)
        builder.connectTimeout(7, TimeUnit.SECONDS)
        builder.addInterceptor(getInterceptor())

        //Your headers
        builder.addInterceptor { chain ->
            var request = chain.request()
            val url = request.url.newBuilder().build()
            request = request.newBuilder().url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Api-Access", "application/mobile")
                .build()
            chain.proceed(request)
        }

        //if debug mood show retrofit logging
        /*if (BuildConfig.DEBUG) {
            val debugInterceptor = HttpLoggingInterceptor()
            debugInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(debugInterceptor)
        }*/

        return builder.build()
    }

    fun getRetrofitObject(): Retrofit? {
        require(!NetworkModel.BASE_URL.isBlank()) {
            "you should provide string value with name 'baseUrl' and give it the base url" }

        if (NetworkModel.retrofit == null)
            NetworkModel.retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .baseUrl(NetworkModel.BASE_URL)
                .client(getOkhttpClient())
                .build()

        return NetworkModel.retrofit
    }
}