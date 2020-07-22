package com.subwilven.basemodel.project_base.base.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

public abstract class BaseDialog : DialogFragment() {

    abstract override fun onCreateDialog(savedInstanceState: Bundle?): Dialog

}

