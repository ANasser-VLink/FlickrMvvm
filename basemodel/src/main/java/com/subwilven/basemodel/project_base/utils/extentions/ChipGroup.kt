package com.subwilven.basemodel.project_base.utils.extentions

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

fun ChipGroup.getIndexOfCheckedChip(checkedId: Int): Int {
    return this.indexOfChild(this.findViewById(checkedId))
}


fun ChipGroup.isCheckable(isCheckable: Boolean) {
    for (index in 0 until this.childCount) {
        (this.getChildAt(index) as Chip).isEnabled = isCheckable
    }
}