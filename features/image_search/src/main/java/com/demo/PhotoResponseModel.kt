package com.demo

import java.io.Serializable

data class PhotoResponseModel(
    val photos: Photos,
    val stat: String
) : Serializable

data class Photos(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    val photo: ArrayList<Photo>
) : Serializable

data class Photo(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val ispublic: Int,
    val isfriend: Int,
    val isfamily: Int
) : Serializable