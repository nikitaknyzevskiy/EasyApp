/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.triare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.triare.triare.ui.TriareView

class TriareViewModelFactor(private val triareView: TriareView) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(TriareView::class.java)
            .newInstance(triareView)
    }

}