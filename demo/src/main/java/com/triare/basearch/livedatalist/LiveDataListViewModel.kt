/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.basearch.livedatalist

import androidx.lifecycle.LiveData
import com.triare.basearch.model.UserModel
import com.triare.basearch.repository.UserRepository
import com.triare.triare.repository.ErrorHandler
import com.triare.triare.ui.TriareView
import com.triare.triare.viewmodel.TriareViewModel

class LiveDataListViewModel(triareView: TriareView) : TriareViewModel(triareView) {

    private val errorHandler = object : ErrorHandler {
        override fun onInternet() {
            triareView.showInternetError {
                triareView.hideError()
                getUsers(true)
            }
        }
        override fun onUnresolved(error: Throwable) {
        }
    }

    private val userRepository = UserRepository(this, errorHandler)

    private var usersLiveData: LiveData<List<UserModel>>? = null

    private fun getUsers(forceLoad: Boolean = false): LiveData<List<UserModel>> {

        if (usersLiveData == null || forceLoad) {
            usersLiveData = userRepository.getUserList()
        }

        return usersLiveData!!
    }

}