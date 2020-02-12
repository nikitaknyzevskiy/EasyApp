/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.basearch.userlivedata


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.triare.basearch.R
import com.triare.triare.ui.TriareView
import com.triare.triare.viewmodel.TriareViewModel

class UserLIveDataFragment : Fragment(), TriareView {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_live_data, container, false)
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showInternetError(tryAgainAction: (() -> Unit)?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showUnresolvedError(error: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
