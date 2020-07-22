package com.subwilven.basemodel.project_base.utils.extentions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

public fun View.show(){
    this.visibility = View.VISIBLE
}

public fun View.hide(){
    this.visibility = View.GONE
}

fun View.hideKeyboard(){
    val inputMethod = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethod.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}