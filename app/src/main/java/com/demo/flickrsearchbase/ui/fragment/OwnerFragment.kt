package com.demo.flickrsearchbase.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.demo.flickrsearchbase.R
import com.demo.flickrsearchbase.ui.viewmodel.OwnerViewModel
import com.demo.flickrsearchbase.utils.OWNER_KEY
import com.demo.flickrsearchbase.utils.OwnerAdapter
import com.subwilven.basemodel.project_base.base.fragments.BaseSuperFragment
import com.subwilven.basemodel.project_base.views.MyRecyclerView

class OwnerFragment : BaseSuperFragment<OwnerViewModel>() {
    override val fragmentTag = "owner"
    private var mAdapter: OwnerAdapter? = null
    private var mRecyclerView: MyRecyclerView? = null
    override val layoutId = R.layout.owner_fragment
    override val viewModelClass = OwnerViewModel::class.java
    override val viewModelLifecycle = this

    override fun setUpObservers() {
        mViewModel.photosLiveList.observe(viewLifecycleOwner, Observer { photos ->
            mAdapter?.addItems(photos)
        })
        /*mViewModel.onImageClicked.observe(viewLifecycleOwner, Observer {
            val bundle = Bundle()
            bundle.putString("owner", viewModel.ownerId.value)
            navigate()
        })*/
    }

    override fun onViewCreated(view: View, viewModel: OwnerViewModel, instance: Bundle?) {


        val args = arguments
        mViewModel.ownerId.value = arguments?.getString(OWNER_KEY)

        viewModel.searchPhotos(R.id.searchRV)

        mAdapter = OwnerAdapter(viewModel)

        mRecyclerView = createRecyclerView(mAdapter!!, recyclerViewId = R.id.ownerRV)
    }

}