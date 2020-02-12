/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.basearch.pagedlist_from_service


import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.triare.basearch.R
import com.triare.basearch.model.UserModel
import com.triare.basearch.ui.ErrorDialog
import com.triare.triare.helper.TAG
import com.triare.triare.helper.createPagedAdapter
import com.triare.triare.helper.generateTriareViewModel
import com.triare.triare.pagged.TriarePaggedAdapter
import com.triare.triare.ui.TriareView
import kotlinx.android.synthetic.main.fragment_paged_list_from.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PagedListFromServiceFragment : Fragment(), TriareView, Observer<PagedList<UserModel>> {

    private val mViewModel: PagedListServiceViewModel by lazy {
        generateTriareViewModel(this, PagedListServiceViewModel::class.java)
    }

    private val pbDialog: ProgressDialog by lazy {
        ProgressDialog(this.context)
    }

    private val errorDialog: ErrorDialog by lazy {
        ErrorDialog()
    }

    @SuppressLint("SetTextI18n")
    private val adapter =
        createPagedAdapter<UserModel>()
            .setView(R.layout.item_user)
            .addBinder { view, item: UserModel? ->
                if (item != null) {
                    val name: TextView = view.findViewById(R.id.user_name)
                    val id: TextView = view.findViewById(R.id.user_id)
                    val email: TextView = view.findViewById(R.id.user_email)
                    val avatar: ImageView = view.findViewById(R.id.user_avatar)

                    name.text = "${getString(R.string.name)} ${item.firstName} ${item.lastName}"
                    id.text = "${getString(R.string.id)} ${item.id}"
                    email.text = "${getString(R.string.email)} ${item.email}"

                    Picasso.get()
                        .load(item.avatar)
                        .into(avatar)
                }
            }
            .addDiffUtil(object : DiffUtil.ItemCallback<UserModel>() {
                override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                   return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                    return oldItem == newItem
                }

            })
            .addKey("id")
            .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paged_list_from, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pagedlist_listt.layoutManager = LinearLayoutManager(this.context).apply {
            orientation = RecyclerView.VERTICAL
        }
        pagedlist_listt.adapter = adapter

        mViewModel.getUserList().observe(this, this)

       // mViewModel.runN()

    }

    override fun showLoading() {
        if (!pbDialog.isShowing)
            pbDialog.show()
    }

    override fun hideLoading() {
        pbDialog.dismiss()
    }

    override fun showInternetError(tryAgainAction: (() -> Unit)?) {
        errorDialog.onRestartClick = {
            tryAgainAction?.invoke()
        }
        errorDialog.show(fragmentManager!!, ErrorDialog::class.java.name)
    }

    override fun hideError() {
        errorDialog.dismiss()
    }

    override fun showUnresolvedError(error: Throwable) {
        Toast.makeText(this.context!!, R.string.unresolved_error, Toast.LENGTH_SHORT).show()
    }

    override fun onChanged(data: PagedList<UserModel>?) {
        adapter.submitList(data)
        if (data != null) {
            Log.d(TAG, "data is not null and size is ${data.size}")
        } else {
            Log.d(TAG, "data is null")
        }
    }


}
