package com.subwilven.basemodel.project_base.utils.extentions

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.gms.maps.internal.StreetViewLifecycleDelegate

fun <T> LiveData<T>.observe(fragment :Fragment,block:(T)->Unit ){
    this.observe(fragment.viewLifecycleOwner,Observer<T>(){
        block.invoke(it)
    })
}