/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.triare.pagged

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.CoroutineContext

class FeedDataFactory <T> (private val onLoad: suspend (page: Int, size: Int) -> List<T>,
                           val onError: (eror: Throwable, redo: FeedDataSource<T>) -> Unit) :
    DataSource.Factory<Int, T>() {

    val mutableLiveData: MutableLiveData<FeedDataSource<*>> = MutableLiveData()

    var feedDataSource: FeedDataSource<T>? = null

    override fun create(): DataSource<Int, T>? {
        feedDataSource = FeedDataSource(onLoad, onError)
        mutableLiveData.postValue(feedDataSource)
        return feedDataSource

    }

}