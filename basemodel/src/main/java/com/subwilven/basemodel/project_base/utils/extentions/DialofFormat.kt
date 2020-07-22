package com.subwilven.basemodel.project_base.utils.extentions

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun DialogFragment.show(fragmentManager: FragmentManager) {

    val tag = javaClass.name

    val transaction = fragmentManager.beginTransaction()
    val prevFragment = fragmentManager.findFragmentByTag(tag)
    if (prevFragment != null) {
        transaction.remove(prevFragment)
    }
    transaction.addToBackStack(tag)
    show(transaction, tag)
}