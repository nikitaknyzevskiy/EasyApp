/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.triare.helper

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.triare.triare.pagged.TriarePaggedAdapter
import com.triare.triare.ui.TriareView
import com.triare.triare.viewmodel.TriareViewModel
import com.triare.triare.viewmodel.TriareViewModelFactor

/**
 * You can get class name of current using class for tag
 */
val Any.TAG : String
    get()  {
        return this::class.java.simpleName
    }

/**
 * Easy way for generate TriareViewModel from fragment
 */
fun <T: TriareViewModel> TriareView.generateTriareViewModel(fragment: Fragment,
                                                            modelClass: Class<T>
): T {
    val triareViewModelFactor = TriareViewModelFactor(this)
    return ViewModelProviders
        .of(fragment, triareViewModelFactor)
        .get(modelClass)
}

/**
 * Easy way for generate TriareViewModel from activity
 */
fun <T: TriareViewModel> TriareView.generateTriareViewModel(fragmentActivity: FragmentActivity,
                                                            modelClass: Class<T>
): T {
    val triareViewModelFactor = TriareViewModelFactor(this)
    return ViewModelProviders
        .of(fragmentActivity, triareViewModelFactor)
        .get(modelClass)
}

/**
 * Create simple paged adapter without creating any classes and view holder
 * you can add diff unit, binder and key (id) if you hadn't add diff unit
 */
fun <MODEL> TriareView.createPagedAdapter ():
        TriarePaggedAdapter<MODEL> {
    return TriarePaggedAdapter()
}