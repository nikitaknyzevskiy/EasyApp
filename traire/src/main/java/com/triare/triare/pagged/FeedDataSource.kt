/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.triare.pagged

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.triare.triare.helper.TAG
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext


class  FeedDataSource <T> (val onLoad: suspend (page: Int, size: Int) -> List<T>,
                           val onErrror: (Throwable, FeedDataSource<T>) -> Unit) : PageKeyedDataSource<Int, T>() {

    val networkState: MutableLiveData<NetworkState> = MutableLiveData<NetworkState>()
    val initialLoading: MutableLiveData<NetworkState> = MutableLiveData<NetworkState>()

    var onRedo: () -> Unit  = {

    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {

        initialLoading.postValue(NetworkState.LOADING)
        networkState.postValue(NetworkState.LOADING)

        Log.d(TAG, "loadInitial")


        onRedo = {
            Log.d(TAG, "onRedo")
            GlobalScope.launch(Dispatchers.Default + errorHandler) {
                val invoke = onLoad.invoke(1, params!!.requestedLoadSize)
                initialLoading.postValue(NetworkState.LOADED)
                networkState.postValue(NetworkState.LOADED)
                callback?.onResult(invoke, null, 2)
            }
        }

        onRedo.invoke()

    }

    private val errorHandler = CoroutineExceptionHandler { context, error ->
        onErrror.invoke(error, this)
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, T>
    ) {

        Log.d(TAG, "loadAfter")

        networkState.postValue(NetworkState.LOADING)

        onRedo = {
            GlobalScope.launch(Dispatchers.Default + errorHandler) {
                val invoke =onLoad.invoke(params.key, params.requestedLoadSize)
                callback.onResult(invoke, params.key + 1)
                networkState.postValue(NetworkState.LOADED)
            }
        }

        onRedo.invoke()
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, T>
    ) {

        Log.d(TAG, "loadAfter")

    }

}

enum class NetworkState {
    LOADING,
    LOADED,
    FAILED
}