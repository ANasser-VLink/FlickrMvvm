package com.subwilven.basemodel.project_base.base.other

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

//example
//total = Transformations.map(CombinedLiveData(subTotal, selectedDeliveryMethod) { it ->
//    val subTotal = it[0] as? Double
//    val deliveryMethod = (it[1] as? DeliveryMethod)
//    val deliveryPrice = if (deliveryMethod?.isFree() !=true) deliveryMethod?.price else 0.0
//    return@CombinedLiveData (deliveryPrice?:0.0) + (subTotal?:0.0)
//}, Function {
//    return@Function it
//})

class CombinedLiveData<R>(
    vararg liveDatas: LiveData<*>,
    private val combine: (datas: List<Any?>) -> R
) : MediatorLiveData<R>() {

    private val datas: MutableList<Any?> = MutableList(liveDatas.size) { null }

    init {
        for (i in liveDatas.indices) {
            super.addSource(liveDatas[i]) {
                datas[i] = it
                value = combine(datas)
            }
        }
    }
}