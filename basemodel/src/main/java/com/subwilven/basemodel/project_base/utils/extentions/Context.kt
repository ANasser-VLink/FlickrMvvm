package com.subwilven.basemodel.project_base.utils.extentions

import android.content.Context
import android.util.TypedValue
import android.widget.Toast
import com.subwilven.basemodel.project_base.POJO.Message

public fun Context.showToastShort(message: Message) {
    showToast(message.getValue(this), Toast.LENGTH_SHORT)
}

public fun Context.showToastLong(
        message: Message) {
    showToast(message.getValue(this), Toast.LENGTH_LONG)
}

public fun Context.showToast(string: String, duration: Int) {
    Toast.makeText(this, string, duration).show()
}

public fun Context.attr(res :Int):Int{
    val typedValue = TypedValue()
    val theme = this.theme
    theme.resolveAttribute(res, typedValue, true)
    return typedValue.data
}