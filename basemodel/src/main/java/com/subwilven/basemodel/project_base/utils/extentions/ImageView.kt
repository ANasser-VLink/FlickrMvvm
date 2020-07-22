package com.subwilven.basemodel.project_base.utils.extentions

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import com.subwilven.basemodel.R
import java.io.File


public fun initPicasso(url: String = "", file: File? = null, @DrawableRes res: Int = -1): RequestCreator {

    val creator = when {
        url.isNotEmpty() -> Picasso.get().load(url)
        file != null -> Picasso.get().load(file)
        res != -1 -> Picasso.get().load(res)
        else -> Picasso.get().load(R.drawable.placeholder)
    }

    creator.placeholder(R.drawable.placeholder)
    creator.error(R.drawable.placeholder)
    creator.fit()
    return creator
}

public fun ImageView.loadImage(url: String?) {
    url?.let{
        val creator = initPicasso(url = url)
        creator.into(this)
    }
}

public fun ImageView.loadImage(file: File?) {
    val creator = initPicasso(file = file)
    creator.into(this)
}

public fun ImageView.loadImage(@DrawableRes res: Int) {
    val creator = initPicasso(res = res)
    creator.into(this)
}

public fun loadImageFromNetwork(imageView: ImageView, url: String) {
    val creator = initPicasso(url)
    creator.into(imageView)
}

public fun loadImageFromFile(imageView: ImageView, file: File) {
    val creator = initPicasso(file = file)
    creator.into(imageView)
}

fun ImageView.setBackGroundColor(@ColorRes colorRes:Int){
    when (background) {
        is ShapeDrawable -> { // cast to 'ShapeDrawable'
            val shapeDrawable = background as ShapeDrawable
            shapeDrawable.paint.color = ContextCompat.getColor(this.context, colorRes)
        }
        is GradientDrawable -> { // cast to 'GradientDrawable'
            val gradientDrawable = background as GradientDrawable
            gradientDrawable.setColor(ContextCompat.getColor(this.context, colorRes))
        }
        is ColorDrawable -> { // alpha value may need to be set again after this call
            val colorDrawable = background as ColorDrawable
            colorDrawable.color = ContextCompat.getColor(this.context, colorRes)
        }
    }
}