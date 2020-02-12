/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.basearch.livedatalist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.triare.basearch.R
import com.triare.triare.helper.generateTriareViewModel
import com.triare.triare.ui.TriareView

class UsersLiveDataFragment : Fragment(), TriareView {

    private val mViewModel: LiveDataListViewModel by lazy {
        generateTriareViewModel(this, LiveDataListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users_live_data, container, false)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showInternetError(tryAgainAction: (() -> Unit)?) {
    }

    override fun hideError() {
    }

    override fun showUnresolvedError(error: Throwable) {
    }


}
