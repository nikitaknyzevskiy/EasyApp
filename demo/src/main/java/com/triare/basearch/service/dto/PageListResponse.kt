/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.basearch.service.dto

import com.google.gson.annotations.SerializedName

data class PageListResponse <RESPONSE> (val data: List<RESPONSE>,
                                        val page: Int,
                                        @SerializedName("per_page") val perPage: Int,
                                        val total: Int,
                                        @SerializedName("total_pages") val totalPages: Int)