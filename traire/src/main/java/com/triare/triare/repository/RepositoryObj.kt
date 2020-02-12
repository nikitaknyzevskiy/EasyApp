/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.triare.repository

import androidx.room.RoomDatabase
import com.triare.triare.Triare
import com.triare.triare.rest.AppRest
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext

abstract class RepositoryObj(errorDis: CoroutineExceptionHandler) {

    /*private val errorDis = CoroutineExceptionHandler { _, error ->
        //errorHandler.invoke(error)
    }*/

    /**
     * Dispatcher for run task in Main third with error handler
     */
    val mainDispatcher = Dispatchers.Main + errorDis

    /**
     * Dispatcher for run task in IO third with error handler
     */
    val ioDispatcher = Dispatchers.IO + errorDis

    /**
     * Dispatcher for run task in Default third with error handler
     */
    val defaultDispatcher = Dispatchers.Default + errorDis

    /**
     * That is your rest api class that generated from Triare.init(...)
     * @see com.triare.triare.Triare
     * with more uses api factor settings
     * If u had not init Triare that can return you
     * @throws IllegalArgumentException("Please init Triare")
     */
    protected inline fun <reified N> restApi() : N {
        val baseUrl = Triare.BASE_URL ?: throw IllegalArgumentException("Please init Triare")
        return AppRest(baseUrl, N::class.java).api()
    }

    /**
     * That is your room database class that generated from Triare.init(...)
     * @see com.triare.triare.Triare
     * If u had not init Triare that can return you
     * @throws IllegalArgumentException("Please init Triare")
     */
    protected inline fun <reified T : RoomDatabase> db() : T {
        val database = Triare.APP_DATABASE ?: throw IllegalArgumentException("Please init Triare")
        return database as T
    }


}