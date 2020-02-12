/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.basearch.repository

import androidx.lifecycle.LiveData
import com.triare.basearch.model.UserModel
import com.triare.basearch.service.ApiService
import com.triare.basearch.service.dto.PageListResponse
import com.triare.triare.repository.ErrorHandler
import com.triare.triare.repository.TriareRepository
import com.triare.triare.viewmodel.TriareViewModel

class UserRepository(viewModel: TriareViewModel, errorHandler: ErrorHandler) :
    TriareRepository(viewModel, errorHandler) {

    fun getUserListResponse(page: Int): PageListResponse<UserModel> {
        return getResponse {
            restApi<ApiService>().getUsers(page)
        }
    }

    fun getUserList(): LiveData<List<UserModel>> {
        return createLiveData {
            fullUserList()
        }
    }

    fun getUser(): LiveData<UserModel> {
        return createLiveData {
            restApi<ApiService>().getUser().data
        }
    }

    private suspend fun fullUserList(): List<UserModel> {
        return restApi<ApiService>().getUsers().data
    }

}