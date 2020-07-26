package com.demo

import com.subwilven.basemodel.project_base.base.other.BaseViewModel
import com.subwilven.basemodel.project_base.base.other.SingleLiveEvent
import com.subwilven.basemodel.project_base.utils.network.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OwnerResultsViewModel : BaseViewModel() {
    private val repo = Repo()
    private val page = SingleLiveEvent<Int>()
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
            if(photosLiveList.value.isNullOrEmpty())
                photosLiveList.value = photosList!!.value!!.photos.photo
            else
                photosLiveList.value?.addAll(photosList!!.value!!.photos.photo)

        }
    }

    fun fetchPhoto(index: Int)  : Photo {
        return photosLiveList.value?.get(index)!!
    }

    fun incrementPage() {
        page.value = page.value?.plus(1)
    }
}