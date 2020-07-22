package com.subwilven.basemodel.project_base.utils.network

import com.google.firebase.FirebaseException
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.subwilven.basemodel.R
import com.subwilven.basemodel.project_base.POJO.ErrorModel
import com.subwilven.basemodel.project_base.POJO.Message
import com.subwilven.basemodel.project_base.base.adapters.AdatperItemLoading
import com.subwilven.basemodel.project_base.base.other.BaseViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

public open class RetrofitCorounites(protected val baseViewModel: BaseViewModel) {

    protected var viewId: Int? = null
    protected var adapterItem: AdatperItemLoading? = null
    protected lateinit var fragmentTag: String
    protected var errorWillBeHandled: Boolean = false

    protected val isStartingFragment: Boolean
        get() = baseViewModel.lastRegisterdFragmentStatus == com.subwilven.basemodel.project_base.POJO.ScreenStatus.STARTING

    constructor(baseViewModel: BaseViewModel, fragmentTag: String, viewId: Int?,errorWillBeHandled :Boolean =false) : this(
        baseViewModel
    ) {
        this.viewId = viewId
        this.fragmentTag = fragmentTag
        this.errorWillBeHandled = errorWillBeHandled
    }

    constructor(
        baseViewModel: BaseViewModel,
        fragmentTag: String,
        adapterItem: AdatperItemLoading?,
        errorWillBeHandled :Boolean =false
    ) : this(baseViewModel) {
        this.adapterItem = adapterItem
        this.fragmentTag = fragmentTag
        this.errorWillBeHandled = errorWillBeHandled
    }

    //handel errors like validation error or authentication error
    private fun getHttpErrorMessage(e: Throwable): String {
        return try {
            val error = (e as HttpException).response()?.errorBody()!!.string()
            val apiError = Gson().fromJson(
                error,
                com.subwilven.basemodel.project_base.POJO.ApiError::class.java
            )
            apiError.error.message
        } catch (e1: Exception) {
            e1.printStackTrace()
            "Cannot Handel Error Message"
        }
    }

    protected fun handelNetworkError(e: Throwable): Boolean {
        when (e) {
            is CancellationException -> {}
//            is HttpException -> {
//                onErrorReceived(getHttpErrorMessage(e), e)
//                return true
//            }
            is SocketTimeoutException -> {
                showError(Message(R.string.ibase_cannot_reach_the_server), ErrorModel.timeOut())
                return true
            }
            is ConnectivityInterceptor.NoConnectivityException -> {
                showError(Message(R.string.ibase_no_network_available), ErrorModel.noConnection())
                return true
            }
            is JsonSyntaxException -> {
                showError(Message(R.string.ibase_server_error))
                return true
            }
            is IOException -> {
                showError(Message(R.string.ibase_something_went_wrong))
                return true
            }
            else -> {
                return if (!errorWillBeHandled) {
                    showError(Message(R.string.ibase_unexpected_error_happened))
                    true
                } else
                    false
            }
        }
        return false
    }

    private fun showError(msg: Message, errorModel: ErrorModel = ErrorModel.serverError(msg)) {

        if (isStartingFragment)
            baseViewModel.showNoConnectionFullScreen(fragmentTag, errorModel)
        else
            baseViewModel.showToastMessage(msg)
    }


    open suspend fun <T> networkCall(block: suspend () -> T): Result<T> {
        return withContext(Dispatchers.Main) {
            try {
                baseViewModel.showLoading(fragmentTag, viewId, adapterItem)
                withContext(Dispatchers.IO) { Success(block.invoke()) }
            } catch (e: Throwable) {
                e.printStackTrace()
                val errorHandled = handelNetworkError(e)
                if (errorHandled)
                    Failure(null)
                else
                    Failure(e)
            } finally {
                baseViewModel.hideLoading(fragmentTag, viewId, adapterItem)
            }
        }
    }

//    suspend fun <T> multipleNetworkCall(blocks: List<Pair<suspend () -> T,(T)->Unit>>): Result<Any> {
//        return withContext(Dispatchers.Main) {
//            try {
//                baseViewModel.showLoading(viewId)
//                val results :MutableList<Deferred<Any>> = mutableListOf()
//                blocks.forEach { results.add(async(Dispatchers.IO) { it.second(it.first.invoke()) })}
//                results.forEach { it.await()}
//                Success("")
//            } catch (e: Throwable) {
//                handelNetworkError(e)
//                Failure()
//            } finally {
//                baseViewModel.hideLoading(viewId)
//            }
//        }
//    }
}