package com.subwilven.basemodel.project_base.utils.extentions

import androidx.viewpager2.widget.ViewPager2

public fun ViewPager2?.nextPage(infinit: Boolean = false): Boolean {
    this?.let { viewPager ->
        val isLastPage = viewPager.currentItem + 1 == viewPager.adapter?.itemCount!!
        if (!isLastPage || infinit) {
            if (isLastPage)
                viewPager.currentItem = 0
            else
                viewPager.currentItem = viewPager.currentItem.plus(1)
            return true
        }
    }
    return false
}