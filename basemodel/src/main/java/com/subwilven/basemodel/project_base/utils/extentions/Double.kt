package com.subwilven.basemodel.project_base.utils.extentions

public fun Double.toString(format :String = "%.2f"):String{
    return String.format(format, this)
}