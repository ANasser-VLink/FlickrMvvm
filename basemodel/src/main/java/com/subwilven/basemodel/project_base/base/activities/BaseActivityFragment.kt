package com.subwilven.basemodel.project_base.base.activities

import android.os.Bundle

import com.subwilven.basemodel.R
import com.subwilven.basemodel.project_base.base.fragments.BaseFragment

abstract class BaseActivityFragment : com.subwilven.basemodel.project_base.base.activities.BaseActivity(){

    override val layoutId = R.layout.ibase_activity_empty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null)
            navigate(supportFragmentManager, getStartFragment())
    }

    abstract fun getStartFragment(): BaseFragment<*>
}