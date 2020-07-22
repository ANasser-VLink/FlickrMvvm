package com.subwilven.basemodel.project_base.utils.network

public sealed class Result<out T>(val value:T?)

public data class Success<out T>(val data: T) : Result<T> (data)

public data class Failure(var error: Throwable? =null) : Result<Nothing>(null)