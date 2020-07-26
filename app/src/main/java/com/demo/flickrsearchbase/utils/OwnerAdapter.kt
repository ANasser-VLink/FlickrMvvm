package com.demo.flickrsearchbase.utils

import android.view.View
import android.view.ViewGroup
import com.demo.flickrsearchbase.R
import com.demo.flickrsearchbase.pojo.Photo
import com.demo.flickrsearchbase.ui.viewmodel.OwnerViewModel
import com.subwilven.basemodel.project_base.base.adapters.BaseAdapter
import com.subwilven.basemodel.project_base.base.adapters.BaseViewHolder
import com.subwilven.basemodel.project_base.utils.extentions.loadImage
import kotlinx.android.synthetic.main.row_item_layout.*


class OwnerAdapter(val mViewModel: OwnerViewModel) : BaseAdapter<Photo, OwnerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnerAdapter.ViewHolder {
        return ViewHolder(parent, R.layout.row_item_layout)
    }

    inner class ViewHolder(viewGroup: ViewGroup, layoutId: Int) :
        BaseViewHolder<Photo>(viewGroup, layoutId), View.OnClickListener {

        override fun onBind(item: Photo) {
            val imageUrl = getImageURL(mViewModel.fetchPhoto(adapterPosition))
            searchImg.loadImage(imageUrl)
        }

        override fun onClick(v: View?) {
        }
    }

    private fun getImageURL(photo: Photo): String {
        val farm = photo.farm
        val server = photo.server
        val id = photo.id
        val secret = photo.secret
        return "https://farm${farm}.staticflickr.com/${server}/${id}_${secret}.jpg"
    }
}