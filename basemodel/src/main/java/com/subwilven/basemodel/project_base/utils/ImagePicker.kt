package com.subwilven.basemodel.project_base.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import java.io.File


object ImagePicker : LifecycleObserver {


    var onPicked: ((imageFile: File?) -> Unit)? = {}
    var onFailed: (()->Unit)? = {}
    fun pickImage(fragment: Fragment, onFailed :(()->Unit)? =null,  onImagePicked: (imageFile: File?) -> Unit) {
        fragment.viewLifecycleOwner.lifecycle.addObserver(this)
        onPicked = onImagePicked
        this.onFailed = onFailed
        fragment.activity?.supportFragmentManager?.beginTransaction()
            ?.add(ImagePickerFragment(), "ImagePickerFragment")
            ?.commitNow()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disconnectListener() {
        onPicked = null
        onFailed= null
    }

    class  ImagePickerFragment : Fragment() {

        private val easyImage : EasyImage?
            get() = EasyImage.Builder(activity!!).build()


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            retainInstance = true
            if (savedInstanceState == null)
                easyImage?.openGallery(this)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            easyImage?.handleActivityResult(requestCode, resultCode, data, activity!!, object : DefaultCallback() {

                override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                    onPicked?.invoke(imageFiles.first().file)
                    activity
                        ?.supportFragmentManager
                        ?.beginTransaction()
                        ?.remove(this@ImagePickerFragment)
                        ?.commitNow()
                }

                override fun onImagePickerError(error: Throwable, source: MediaSource) {
                    super.onImagePickerError(error, source)
                    error.printStackTrace()
                    onFailed?.invoke()
                    activity
                        ?.supportFragmentManager
                        ?.beginTransaction()
                        ?.remove(this@ImagePickerFragment)
                        ?.commitNow()
                }

                override fun onCanceled(source: MediaSource) {
                    super.onCanceled(source)
                    activity
                        ?.supportFragmentManager
                        ?.beginTransaction()
                        ?.remove(this@ImagePickerFragment)
                        ?.commitNow()
                }

            })
        }
    }
}