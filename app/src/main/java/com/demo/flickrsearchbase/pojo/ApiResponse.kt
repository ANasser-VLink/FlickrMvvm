package com.demo.flickrsearchbase.pojo

data class ApiResponse<T> (val status:String,val sources: T)
