package com.demo.flickrsearchbase.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.demo.flickrsearchbase.data.Repo
import com.demo.flickrsearchbase.pojo.Photo
import com.demo.flickrsearchbase.pojo.PhotoResponseModel
import com.subwilven.basemodel.project_base.base.other.BaseViewModel
import com.subwilven.basemodel.project_base.base.other.SingleLiveEvent
import com.subwilven.basemodel.project_base.utils.network.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OwnerViewModel : BaseViewModel() {
    private val repo = Repo()
    val page = SingleLiveEvent<Int>()
    val ownerId = SingleLiveEvent<String>()
    var photosList: Result<PhotoResponseModel>? = null
    var photosLiveList = SingleLiveEvent<ArrayList<Photo>>()
    var oldCount = SingleLiveEvent<Int>()
    init {
        page.value = 1
    }

    fun searchPhotos(viewId: Int?) {
        appScope.launch {
            delay(2000)
            photosList = networkCall(viewId) { repo.fetchOwners(ownerId.value ?: "" ,page.value) }
            // providersList = networkCall(viewId) { repository.getProvidersList() }
            markAsCompleted(listOf(photosList!!))
            oldCount.value = photosLiveList.value?.count()
            photosLiveList.value?.addAll(photosList!!.value!!.photos.photo)

        }
    }

    fun fetchPhoto(index: Int)  : Photo {
        return photosLiveList.value?.get(index)!!
    }
}