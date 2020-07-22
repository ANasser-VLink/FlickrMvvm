package com.subwilven.basemodel.project_base.base.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.subwilven.basemodel.R

import com.subwilven.basemodel.project_base.base.fragments.BaseFragment
import com.subwilven.basemodel.project_base.base.other.BaseViewModel


abstract class BaseFullScreenDialog : DialogFragment() {

    protected abstract var layoutId: Int
    protected abstract var style: Int
    protected open var slideFromBottom: Boolean = true
    protected open var exchangeViewRoot: Boolean = true
    protected var viewModel: BaseViewModel? = null

    abstract fun onDialogCreated(view: View?, savedInstanceState: Bundle?)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            style
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mView = inflater.inflate(layoutId, container, false)
        parentFragment?.let {
            viewModel = ((parentFragment as BaseFragment<*>).mViewModel)
            if (exchangeViewRoot) {
                (parentFragment as BaseFragment<*>).exchangeRootViewToDialogView(mView!!)
            }
        }
        onDialogCreated(mView, savedInstanceState)
        return mView
    }


    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            if (slideFromBottom)
                dialog.window!!.setWindowAnimations(R.style.Slide);
        }
    }

    //set fragment root view to be the dialog view so when searching for loading views we can find it
    override fun onDismiss(dialog: DialogInterface) {
        parentFragment?.let {
            if (exchangeViewRoot)
                (parentFragment as BaseFragment<*>).exchangeRootViewToFragmentView()
        }
        super.onDismiss(dialog)
    }
}