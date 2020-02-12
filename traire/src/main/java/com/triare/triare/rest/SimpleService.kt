/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.triare.rest

import retrofit2.http.GET

interface SimpleService {

    @GET("")
    suspend fun getNek(): Any

}