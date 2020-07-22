package com.subwilven.basemodel.project_base.base.adapters

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.subwilven.basemodel.R


import com.subwilven.basemodel.project_base.views.MyRecyclerView
import com.subwilven.basemodel.project_base.views.OnViewStatusChange


public abstract class BasePagingAdapter<T, VH : BaseViewHolder<T>> protected constructor(callback : DiffUtil.ItemCallback<T>)
    : PagedListAdapter<T, BaseViewHolder<T>>(callback)
        , OnViewStatusChange,LifecycleObserver{

    // private var networkState: NetworkRequestState? = null
    private var hasExtraItems: Boolean = false

    var adapterDataObservation: AdapterDataObservation?=null

    fun registerAdapterDataObservertion(lifecycleOwner: LifecycleOwner, recyclerView: MyRecyclerView) {
        lifecycleOwner.lifecycle.addObserver(this)
        adapterDataObservation = AdapterDataObservation(recyclerView)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun connectListener() {
        registerAdapterDataObserver(adapterDataObservation!!)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnectListener() {
        unregisterAdapterDataObserver(adapterDataObservation!!)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clearAdapterDataObservation(){
        adapterDataObservation?.clear()
    }


    abstract fun getViewHolder(viewGroup: ViewGroup): VH

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        return when (viewType) {
            TYPE_PROGRESS -> LoadingViewHolder(parent)
            else -> getViewHolder(parent)
        }

    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        getItem(position)?.let {
            holder.onBind(it)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraItems && position == itemCount-1) {
            TYPE_PROGRESS
        } else {
            TYPE_ITEM
        }
    }

    override fun showLoading(b: Boolean) {
        hasExtraItems = b
        if (b)
            notifyItemInserted(itemCount)
        else
            notifyItemChanged(itemCount)
    }

    inner class LoadingViewHolder(itemView: ViewGroup) : BaseViewHolder<T>(itemView, R.layout.ibase_item_loading) {
        override fun onBind(item: T) {}
    }

    companion object {
        private val TYPE_PROGRESS = 0
        private val TYPE_ITEM = 1

        internal var callback: DiffUtil.ItemCallback<Any> = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return false
            }

        }
    }

}
