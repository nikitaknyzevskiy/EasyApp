/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.triare.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.room.RoomDatabase
import com.triare.triare.Triare
import com.triare.triare.helper.TAG
import com.triare.triare.rest.AppRest
import com.triare.triare.viewmodel.TriareViewModel
import kotlinx.coroutines.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class TriareRepository(private val viewModel: TriareViewModel,
                                private val errorHandler: ErrorHandler)
    : RepositoryObj(CoroutineExceptionHandler { context, error ->

        if (error is UnknownHostException ||
            error is SocketTimeoutException ||
            error is ConnectException) {
            errorHandler.onInternet()
        } else {
            errorHandler.onUnresolved(error)
        }

    })
{

    open fun onShowProgress() {
        viewModel.onShowProgress()
    }

    open fun onHideProgress() {
        viewModel.onHideProgress()
    }

    /**
     * that handler to u unresolved error
     * u also can handler it to user by
     * @see com.triare.triare.ui.TriareView#showUnresolvedError(...)
     */
    open fun onErrorHandler(error: Throwable) {
        Log.e(TAG, "onErrorHandler", error)
        viewModel.triareView.showUnresolvedError(error)
    }

    /**
     * Use that function for run your suspend task with progress bar, errorHandel and call back if need
     * first on mainDispatcher run onShowProgress() func than with ioDispatcher  context run your
     * suspend task that u sat on constructor
     * @param onLoad
     * next run
     * @onDone if you implemented
     * at lat onHideProgress()
     * Major uses is for db task suck as save, delete and update
     */
    open fun runSuspendTask(onLoad: suspend () -> Unit, onDone: (() -> Unit)? = null)
            = runBlocking(mainDispatcher) {
        onShowProgress()
        withContext(ioDispatcher) {
            onLoad.invoke()
        }
        onDone?.invoke()
        onHideProgress()
    }

    /**
     * Use that function for run your suspend task with errorHandel and call back if need
     * first on mainDispatcher run with ioDispatcher context run your
     * suspend task that u sat on constructor
     * @param onLoad
     * next run
     * @onDone if you implemented
     * Major uses is for db task suck as save, delete and update
     */
    open fun runSuspendTaskNoProgress(onLoad: suspend () -> Unit, onDone: (() -> Unit)? = null)
            = runBlocking(ioDispatcher) {
        withContext(defaultDispatcher) { onLoad.invoke() }
        withContext(defaultDispatcher) { onDone?.invoke() }
    }

    /**
     * Use function for response as live data value from your suspend function
     * with progress bar and error handel inside ioDispatcher context
     * for example to display as live data value from your api service
     */
    open fun <T> createLiveData(onLoad: suspend () -> T): LiveData<T> {
        return liveData(mainDispatcher) {
            onShowProgress()
            withContext(ioDispatcher) {
                emit(onLoad.invoke())
            }
            onHideProgress()
        }
    }

    /**
     * Use function for response as live data value from your suspend function
     * with error handel inside ioDispatcher context
     * for example to display as live data value from your api service
     */
    open fun <T> createLiveDataNoProgress(onLoad: suspend () -> T): LiveData<T> {
        return liveData(ioDispatcher) {
            emit(onLoad.invoke())
        }
    }

    /**
     * Use function for response as value from your suspend function
     * with progress bar and error handel inside ioDispatcher context
     * for example to display as value from your api service
     */
    open fun <T> getResponse(onLoad: suspend () -> T): T
            = runBlocking(ioDispatcher) {

        //TODO(REDO)

        withContext(mainDispatcher) {
            onShowProgress()
        }

        val data = async(ioDispatcher) {
            onLoad
                .invoke()
        }

        withContext(mainDispatcher) {
            onHideProgress()
        }

        //reDoTaskAction = null

        data.await()
    }

    /**
     * Use function for response as value from your suspend function
     * with error handel inside ioDispatcher context
     * for example to display as value from your api service
     */
    open fun <T> getResponseNoProgress(onLoad: suspend () -> T, redoAction: (() -> Unit)? = null): T
            = runBlocking(ioDispatcher) {

        if (redoAction != null) {
            //TODO(REDO)
        }

        val data = onLoad
            .invoke()
        data
    }


}