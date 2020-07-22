package com.demo.flickrsearchbase.ui.main

import android.os.Bundle
import com.demo.flickrsearchbase.R
import com.demo.flickrsearchbase.ui.fragment.ImageSearchFragment
import com.subwilven.basemodel.project_base.base.activities.BaseActivity

class MainActivity : BaseActivity() {
    override val layoutId = R.layout.main_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigate(supportFragmentManager ,
            ImageSearchFragment(), addToBackStack = false)
    }

}