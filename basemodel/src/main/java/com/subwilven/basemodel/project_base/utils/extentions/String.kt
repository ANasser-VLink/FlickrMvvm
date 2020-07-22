package com.subwilven.basemodel.project_base.utils.extentions

import android.text.TextUtils
import com.subwilven.basemodel.R
import java.lang.Double.parseDouble

public fun String.isValidEmail(): Int {
    return if (TextUtils.isEmpty(this)) {
        R.string.ibase_all_fields_are_required
    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()) {
        R.string.ibase_email_not_valid
    } else
        -1
}

public fun String.isValidPassword(): Int {
    return when {
        TextUtils.isEmpty(this) -> R.string.ibase_all_fields_are_required
        this.length < 6 -> R.string.ibase_password_should_be_at_least_6_charachter
        else -> -1
    }
}

fun String.isValidName(): Int {
    return when {
        TextUtils.isEmpty(this) -> R.string.ibase_all_fields_are_required
        this.length < 3 -> R.string.name_is_short
        else -> -1
    }
}