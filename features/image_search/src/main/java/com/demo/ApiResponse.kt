package com.demo

data class ApiResponse<T> (val status:String,val sources: T)
