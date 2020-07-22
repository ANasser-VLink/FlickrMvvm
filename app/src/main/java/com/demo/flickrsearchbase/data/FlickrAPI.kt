package com.demo.flickrsearchbase.data

import com.demo.flickrsearchbase.pojo.ApiResponse
import com.demo.flickrsearchbase.pojo.PhotoResponseModel
import com.demo.flickrsearchbase.utils.API_KEY
import com.demo.flickrsearchbase.utils.API_METHOD
import com.demo.flickrsearchbase.utils.PER_PAGE
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrAPI {

    @GET("rest/")
    suspend fun searchPhotos(
        @Query("method") method: String ? = API_METHOD,
        @Query("api_key") api_key: String? = API_KEY,
        @Query("format") format: String? = "json",
        @Query("text") text: String? =  null,
        @Query("page") page: Int? = 1,
        @Query("per_page") perpage: Int? = PER_PAGE,
        @Query("nojsoncallback") isCallBack: Int = 1
    ) : PhotoResponseModel

    @GET("rest/")
    suspend fun getPicsOfOwner(
        @Query("method") method: String ? = API_METHOD,
        @Query("api_key") api_key: String? = API_KEY,
        @Query("format") format: String? = "json",
        @Query("user_id") userId: String? =  null,
        @Query("page") page: Int? = 1,
        @Query("per_page") perpage: Int? = PER_PAGE,
        @Query("nojsoncallback") isCallBack: Int = 1
    ) : PhotoResponseModel
}