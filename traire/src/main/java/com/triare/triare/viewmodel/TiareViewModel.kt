/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.triare.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.room.RoomDatabase
import com.triare.triare.Triare
import com.triare.triare.helper.TAG
import com.triare.triare.pagged.FeedDataFactory
import com.triare.triare.pagged.FeedDataSource
import com.triare.triare.repository.TriareRepository
import com.triare.triare.rest.AppRest
import com.triare.triare.ui.TriareView
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.suspendCoroutine


/**
 * ViewModel that contains:
 * 1) progress bar and error handler call backs of TriareView
 * @see com.triare.triare.ui.TriareView
 * 2) Generate live data fro suspend functions suck as for db tasks
 * 3) Rest api and database objects that you init by Triare.init(...)
 * @see com.triare.triare.Triare
 * For use TriareViewModel you need implemented from your ViewModel
 * Please provide TriareViewModels via
 * @see com.triare.triare.viewmodel.TriareViewModelFactor cause u add to constructor
 * @param triareView
*/
abstract class TriareViewModel(val triareView: TriareView) : ViewModel() {

    /**
     * If do u sat reDoTaskAction from some where when app  got internet error such as lost connection,
     * user can run task action for redo task
    */
    open var reDoTaskAction: (() -> Unit)? = null

    open fun onShowProgress() {
        Log.d(TAG, "onShowProgress")
        triareView.showLoading()
    }

    open fun onHideProgress() {
        Log.d(TAG, "onHideProgress")
        triareView.hideLoading()
    }

    /**
     * that handler to u unresolved error
     * u also can handler it to user by
     * @see com.triare.triare.ui.TriareView#showUnresolvedError(...)
     */
    /*open fun onErrorHandler(error: Throwable) {
        Log.e(TAG, "onErrorHandler", error)
        triareView.showUnresolvedError(error)
    }*/

    /**
     * Generate paged list live data.
     * Uses for response your data from service with pagination without write any scroll listener for
     * your recycle view and a lot of logic.
     * Everything are contains here
     */
    fun <T, RESPONSE> createPagedResponseLiveDate(pageSize: Int,
                                                  onLoad: suspend (page: Int,
                                                                    size: Int) -> RESPONSE,
                                                  onMap: (RESPONSE) -> List<T>,
                                                  onCatch: ( (error: Throwable, redo: () -> Unit ) -> Unit )? = null)
            : LiveData<PagedList<T>> {

        val feedDataFactory: FeedDataFactory<T> = FeedDataFactory(
            { page, size ->
                val resposne = onLoad.invoke(page, size)

                return@FeedDataFactory onMap.invoke(resposne)
            },
            { error, redo ->
                onCatch?.invoke(error, redo.onRedo)
            }
        )


        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(2)
            .setPageSize(pageSize)
            .build()

        return LivePagedListBuilder(feedDataFactory, pagedListConfig)
            //.setFetchExecutor(Executors.newFixedThreadPool(5))
            .build()
    }

    /**
     * Stop all task and cancel dispatcher if uses when viewModel is stop
     */
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
        /*mainDispatcher.cancel()
        ioDispatcher.cancel()
        defaultDispatcher.cancel()*/
    }

}