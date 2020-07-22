package com.subwilven.basemodel.project_base.base.adapters

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.subwilven.basemodel.project_base.views.MyRecyclerView


abstract class BaseListAdapter<T, VH : BaseViewHolder<T>>











    (diffCallback: DiffUtil.ItemCallback<T>) : ListAdapter<T, VH>(diffCallback),LifecycleObserver {

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

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        holder.onBind(getItem(position),payloads)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(getItem(position))
    }

}
