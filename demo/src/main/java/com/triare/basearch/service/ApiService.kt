/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.basearch.service

import com.triare.basearch.model.UserModel
import com.triare.basearch.service.dto.PageListResponse
import com.triare.basearch.service.dto.SingleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/api/users")
    suspend fun getUsers(@Query("page") page: Int): PageListResponse<UserModel>

    @GET("/api/users")
    suspend fun getUsers(): PageListResponse<UserModel>

    @GET("/users/2")
    suspend fun getUser(): SingleResponse<UserModel>

}