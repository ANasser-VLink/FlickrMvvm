package com.demo

import com.subwilven.basemodel.project_base.utils.network.NetworkModel
import kotlinx.coroutines.delay

class Repo {

    suspend fun searchPhotos(queryText: String, page:Int? = 1): PhotoResponseModel {
            delay(2000)
            return NetworkModel.getService<FlickrAPI>().searchPhotos(API_METHOD, API_KEY, page = page, text = queryText)
    }
    suspend fun fetchOwners(userId: String, page:Int? = 1): PhotoResponseModel {
            delay(2000)
            return NetworkModel.getService<FlickrAPI>().getPicsOfOwner(API_METHOD, API_KEY, page = page, userId = userId)
    }
}