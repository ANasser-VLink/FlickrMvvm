package com.demo.flickrsearchbase.ui.viewmodel

import android.util.Log
import com.demo.flickrsearchbase.data.Repo
import com.demo.flickrsearchbase.pojo.Photo
import com.demo.flickrsearchbase.pojo.PhotoResponseModel
import com.subwilven.basemodel.project_base.base.other.BaseViewModel
import com.subwilven.basemodel.project_base.base.other.SingleLiveEvent
import com.subwilven.basemodel.project_base.utils.network.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ImageSearchViewModel : BaseViewModel() {
    private val repo = Repo()
    val page = SingleLiveEvent<Int>()
    val queryText= SingleLiveEvent<String>()
    var photosList: Result<PhotoResponseModel>? = null
    var photosLiveList = SingleLiveEvent<ArrayList<Photo>>()
    var onImageClicked = SingleLiveEvent<Boolean>()
    var ownerId = SingleLiveEvent<String>()
    var oldCount = SingleLiveEvent<Int>()
    init {
        page.value = 1
    }

    fun searchPhotos(viewId: Int?) {
        appScope.launch {
            delay(2000)
            photosList = networkCall(viewId) { repo.searchPhotos(queryText.value ?: "" ,page.value) }
            // providersList = networkCall(viewId) { repository.getProvidersList() }
            markAsCompleted(listOf(photosList!!))
            oldCount.value = photosLiveList.value?.count()
            if(photosLiveList.value.isNullOrEmpty())
                photosLiveList.value = photosList!!.value!!.photos.photo
            else
                photosLiveList.value?.addAll(photosList!!.value!!.photos.photo)
            Log.v("asdf", photosLiveList.value.toString())
        }
    }

    fun fetchPhoto(index: Int)  : Photo {
        return photosLiveList.value?.get(index)!!
    }
}