package com.subwilven.basemodel.project_base.utils

import android.Manifest
import android.annotation.SuppressLint
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.subwilven.basemodel.R

import com.subwilven.basemodel.project_base.BaseApplication
import com.subwilven.basemodel.project_base.base.fragments.BaseFragment

public object PermissionsManager {

    val READ_SMS = Manifest.permission.READ_SMS
    val READ_CONTACTS = Manifest.permission.READ_CONTACTS
    val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    val NETWORK_STATE = Manifest.permission.ACCESS_NETWORK_STATE
    val INTERNET = Manifest.permission.INTERNET
    val CAMERA = Manifest.permission.CAMERA
    val CONTACTS = Manifest.permission.READ_CONTACTS
    val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    val READ_PHONE_NUMBERS = Manifest.permission.READ_PHONE_STATE
    val GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS

    @SuppressLint("CheckResult")
    fun requestPermission(fragment: BaseFragment<*>,
                          vararg permissions: String?,
                          onGranted: (() -> Unit)? = null,
                          onDenied: (() -> Unit)? = null) {

        TedPermission.with(fragment.context)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        onGranted?.invoke()
                    }

                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                        onDenied?.invoke()
                    }

                })
                .setDeniedMessage(BaseApplication.instance?.getString(R.string.you_cannot_user_this_permission))
                .setPermissions(*permissions)
                .check()

    }
}

