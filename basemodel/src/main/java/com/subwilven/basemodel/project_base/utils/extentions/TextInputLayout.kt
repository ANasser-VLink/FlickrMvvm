package com.subwilven.basemodel.project_base.utils.extentions

import com.google.android.material.textfield.TextInputLayout

public fun TextInputLayout.getText():String{
    return this.editText?.text.toString()
}