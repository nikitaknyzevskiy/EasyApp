/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.basearch.pagedlist_from_service

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.triare.basearch.model.UserModel
import com.triare.basearch.repository.UserRepository
import com.triare.basearch.service.ApiService
import com.triare.basearch.service.dto.PageListResponse
import com.triare.triare.ui.TriareView
import com.triare.triare.viewmodel.TriareViewModel
import com.triare.triare.repository.ErrorHandler

class PagedListServiceViewModel(triareView: TriareView) : TriareViewModel(triareView) {

    val errorHandler = object : ErrorHandler {
        override fun onInternet() {
            triareView.showInternetError {}
        }

        override fun onUnresolved(error: Throwable) {
            triareView.showUnresolvedError(error)
        }
    }

    private val userRepository = UserRepository(this, errorHandler)

    fun getUserList(): LiveData<PagedList<UserModel>> {
        return createPagedResponseLiveDate(
            2,
            { page: Int, size: Int ->
                return@createPagedResponseLiveDate userRepository.getUserListResponse(page)
            },
            { response: PageListResponse<UserModel> ->
                return@createPagedResponseLiveDate response.data
            },
            { error, redo  ->
                triareView.showInternetError {
                    redo.invoke()
                }
            })
    }

}