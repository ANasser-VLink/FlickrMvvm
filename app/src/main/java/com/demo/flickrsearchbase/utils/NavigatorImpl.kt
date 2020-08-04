package com.demo.flickrsearchbase.utils

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.demo.NavigationListener
import com.demo.flickrsearchbase.ui.fragment.OwnerFragment
import com.subwilven.basemodel.R

class   NavigatorImpl(private val fragmentManager: FragmentManager) :NavigationListener {
    override fun onBtnClicked(userId:String) {
        val fragment = OwnerFragment()
        val args = Bundle()
        args.putString("userId",userId)
        fragment.arguments = args
        fragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack("welcome")
            .commit()
    }


}