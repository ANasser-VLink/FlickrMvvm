package com.subwilven.basemodel.project_base.base.dialogs

import android.app.Dialog
import android.os.Bundle

public class BaseDefaultDialog(defaultDialog: Dialog) : BaseDialog() {

    var defaultDialog: Dialog? = defaultDialog


    init {
        retainInstance = true
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return defaultDialog!!
    }

    override fun onDestroyView() {
        defaultDialog =null
        super.onDestroyView()
    }
}