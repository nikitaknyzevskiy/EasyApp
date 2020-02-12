/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.triare.pagged

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.triare.triare.ui.TriareView
import org.json.JSONObject

class TriarePaggedAdapter <MODEL> {

    fun setView(@LayoutRes itemView: Int): BuilderView<MODEL> {
        return BuilderView(itemView)
    }

    fun <VH : RecyclerView.ViewHolder> setView(@LayoutRes itemView: Int,
                                               viewHolder: Class<VH>)
            : BuilderHolder<MODEL, VH> {
        return BuilderHolder(itemView, viewHolder)
    }

    class BuilderView <MODEL> (@LayoutRes val itemView: Int ): Builder<MODEL>()
    {
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        private var binder: (view: View, item: MODEL?) -> Unit = { _: View, _: MODEL? ->

        }

        fun addBinder(binder: (view: View, item: MODEL?) -> Unit): Builder<MODEL>  {
            this.binder = binder
            return this
        }

        fun build(): PagedListAdapter<MODEL, RecyclerView.ViewHolder> {
            if (diffUtil == null) {
                diffUtil = defaultDiffUtil
            }
            return object : PagedListAdapter<MODEL, RecyclerView.ViewHolder>(diffUtil!!) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): RecyclerView.ViewHolder {
                    val view = LayoutInflater.from(parent.context).inflate(
                        itemView,
                        parent,
                        false
                    )
                    return ViewHolder(view)
                }
                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                    binder.invoke(holder.itemView, getItem(position))
                }
            }
        }
    }

    class BuilderHolder <MODEL, VH : RecyclerView.ViewHolder> (@LayoutRes val itemView: Int,
                                                               val viewHolder: Class<VH>) : Builder<MODEL>()
    {
        private var binder: (viewHolder: VH, item: MODEL?) -> Unit = { _: VH, _: MODEL? ->

        }

        fun addBinder(binder: (view: VH, item: MODEL?) -> Unit): Builder<MODEL>  {
            this.binder = binder
            return this
        }

        fun build(): PagedListAdapter<MODEL, VH> {
            if (diffUtil == null) {
                diffUtil = defaultDiffUtil
            }
            return object : PagedListAdapter<MODEL, VH>(diffUtil!!) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): VH {
                    val view = LayoutInflater.from(parent.context).inflate(
                        itemView,
                        parent,
                        false
                    )
                    return viewHolder.getConstructor(View::class.java)
                        .newInstance(view)
                }
                override fun onBindViewHolder(holder: VH, position: Int) {
                    binder.invoke(holder, getItem(position))
                }
            }
        }
    }

    abstract class Builder <MODEL> {

        protected val defaultDiffUtil: DiffUtil.ItemCallback<MODEL>
                by lazy {
                    object : DiffUtil.ItemCallback<MODEL>() {
                        override fun areItemsTheSame(oldItem: MODEL, newItem: MODEL): Boolean {
                            val oldJsonItem = JSONObject(Gson().toJson(oldItem))
                            val newJsonItem = JSONObject(Gson().toJson(oldItem))
                            return if (oldJsonItem.has(keyName) && newJsonItem.has(keyName)) {
                                oldJsonItem.get(keyName) == newJsonItem.get(keyName)
                            } else {
                                oldItem == newItem
                            }
                        }

                        override fun areContentsTheSame(oldItem: MODEL, newItem: MODEL): Boolean {
                            val gson = Gson()
                            return gson.toJson(oldItem) == gson.toJson(newItem)
                        }

                    }
                }

        protected var keyName = "id"

        fun addKey(key: String): Builder<MODEL> {
            keyName = key
            return this
        }

        fun addDiffUtil(diffUtil: DiffUtil.ItemCallback<MODEL>): Builder<MODEL> {
            this.diffUtil = diffUtil
            return this
        }

        protected var diffUtil: DiffUtil.ItemCallback<MODEL>? = null

    }

}