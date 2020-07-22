package com.subwilven.basemodel.project_base.utils.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.subwilven.basemodel.BuildConfig

import com.subwilven.basemodel.project_base.BaseApplication
import com.subwilven.basemodel.project_base.utils.extentions.attr
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

public object NetworkModel {

    var BASE_URL= "https://www.flickr.com/services/"
    var retrofit: Retrofit? = null
   // var clientApi: Any? = null

    //TODO make dynamic static variable to prevent recreation of the same instance
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
        if (BuildConfig.DEBUG) {
            val debugInterceptor = HttpLoggingInterceptor()
            debugInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(debugInterceptor)
        }

        return builder.build()
    }

    fun getRetrofitObject(): Retrofit? {
     require(!BASE_URL.isBlank()) {
         "you should provide string value with name 'baseUrl' and give it the base url" }

        if (retrofit == null)
            retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .baseUrl(BASE_URL)
                    .client(getOkhttpClient())
                    .build()

        return retrofit
    }

}