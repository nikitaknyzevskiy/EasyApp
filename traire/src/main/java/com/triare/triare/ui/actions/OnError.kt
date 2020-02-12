/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.triare.ui.actions

interface OnError {
    fun showInternetError(tryAgainAction: (() -> Unit)?)

    fun hideError()

    fun showUnresolvedError(error: Throwable)
}