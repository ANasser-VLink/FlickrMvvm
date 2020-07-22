package com.subwilven.basemodel.project_base.utils

import android.app.Dialog
import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.*

import com.subwilven.basemodel.R
import com.subwilven.basemodel.project_base.POJO.Message
import com.subwilven.basemodel.project_base.base.dialogs.BaseDefaultDialog
import com.subwilven.basemodel.project_base.utils.extentions.show

public interface DialogManager {

    val _fragmentManager: FragmentManager?
    val _context: Context

    private fun buildDialog(
        @StringRes title: Int,
        @StringRes positiveButton: Int = -1,
        @StringRes negativeButton: Int = -1,
        isBottomSheet: Boolean = false,
        cancelable: Boolean = true,
        cancelOnTouchOutside: Boolean = true,
        onPositiveClick: ((dialog:MaterialDialog) -> Unit)? = null,
        onNegativeClick: ((dialog:MaterialDialog) -> Unit)? = null
    ): MaterialDialog {

        val dialog =
            if (isBottomSheet) {
                MaterialDialog(_context, BottomSheet((LayoutMode.WRAP_CONTENT))).title(title)
            } else {
                MaterialDialog(_context).title(title)
            }

                .cancelOnTouchOutside(cancelOnTouchOutside)
                .cancelable(cancelable)
        if (positiveButton != -1)
            dialog.positiveButton(positiveButton) { onPositiveClick?.invoke(it) }

        if (negativeButton != -1)
            dialog.negativeButton(negativeButton) { onNegativeClick?.invoke(it) }

        return dialog
    }

    private fun show(dialog: Dialog, fragmentManager: FragmentManager) {
        BaseDefaultDialog(dialog).show(fragmentManager)
    }


    fun showDialog(
        @StringRes title: Int,
        message: Message,
        @StringRes positiveButton: Int = R.string.ibase_ok,
        @StringRes negativeButton: Int = -1,
        isBottomSheet: Boolean = false,
        cancelable: Boolean = true,
        cancelOnTouchOutside: Boolean = true,
        onNegativeClick: ((dialog:MaterialDialog) -> Unit)? = null,
        onPositiveClick: ((dialog:MaterialDialog) -> Unit)? = null
    ) {
        val dialog = buildDialog(
            title,
            positiveButton,
            negativeButton,
            isBottomSheet,
            cancelable,
            cancelOnTouchOutside,
            onPositiveClick,
            onNegativeClick
        )
        dialog.message(text = message.getValue(_context))
        show(dialog, _fragmentManager!!)
    }

    fun showDialogList(
        @StringRes title: Int,
        items: List<String>,
        isBottomSheet: Boolean = false,
        cancelable: Boolean = true,
        cancelOnTouchOutside: Boolean = true,
        onItemSelectedListener: ((dialog: MaterialDialog, index: Int, text: String) -> Unit)? = null
    ) {
        var dialog = buildDialog(
            title,
            isBottomSheet = isBottomSheet,
            cancelable = cancelable,
            cancelOnTouchOutside = cancelOnTouchOutside
        )
        dialog = dialog.listItems(
            waitForPositiveButton = false,
            items = items,
            selection = onItemSelectedListener as? ItemListener?
        )
        show(dialog, _fragmentManager!!)
    }

    fun showDialogListSingleChoice(
        @StringRes title: Int,
        items: List<String>,
        @StringRes positiveButton: Int = -1,
        @StringRes negativeButton: Int = -1,
        isBottomSheet: Boolean = false,
        waitForPositiveButton: Boolean = true,
        cancelable: Boolean = true,
        cancelOnTouchOutside: Boolean = true,
        initialSelection: Int = -1,
        onNegativeClick: ((dialog:MaterialDialog) -> Unit)? = null,
        onPositiveClick: ((dialog: MaterialDialog, index: Int, text: String) -> Unit)? = null
    ) {
        var dialog = buildDialog(
            title,
            positiveButton,
            negativeButton,
            isBottomSheet,
            cancelable,
            cancelOnTouchOutside,
            onNegativeClick = onNegativeClick
        )

        dialog = dialog.listItemsSingleChoice(
            items = items,
            waitForPositiveButton = waitForPositiveButton,
            initialSelection = initialSelection,
            selection = onPositiveClick as? SingleChoiceListener?
        )

        show(dialog, _fragmentManager!!)
    }

    fun showDialogListMultiChoice(
        @StringRes title: Int,
        items: List<String>,
        @StringRes positiveButton: Int = R.string.ibase_ok,
        @StringRes negativeButton: Int = -1,
        isBottomSheet: Boolean = false,
        cancelable: Boolean = true,
        cancelOnTouchOutside: Boolean = true,
        initialSelection: IntArray = IntArray(0),
        onNegativeClick: ((dialog:MaterialDialog) -> Unit)? = null,
        onPositiveClick: ((dialog: MaterialDialog, indices: IntArray, items: List<String>) -> Unit)? = null
    ) {
        var dialog = buildDialog(
            title,
            positiveButton,
            negativeButton,
            isBottomSheet,
            cancelable,
            cancelOnTouchOutside,
            onNegativeClick = onNegativeClick
        )

        dialog = dialog.listItemsMultiChoice(
            items = items,
            allowEmptySelection = false,
            initialSelection = initialSelection,
            selection = onPositiveClick as? MultiChoiceListener?
        )

        show(dialog, _fragmentManager!!)
    }

}