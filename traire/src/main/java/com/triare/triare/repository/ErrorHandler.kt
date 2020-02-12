/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.triare.repository

interface ErrorHandler {
    fun onInternet()
    fun onUnresolved(error: Throwable)
}