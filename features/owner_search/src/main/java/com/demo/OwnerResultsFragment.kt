package com.demo

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.demo.owner_search.R
import com.subwilven.basemodel.project_base.base.fragments.BaseSuperFragment
import com.subwilven.basemodel.project_base.views.MyRecyclerView

class OwnerResultsFragment : BaseSuperFragment<OwnerResultsViewModel>() {
    override val fragmentTag = "owner"
    private var mAdapter: OwnerAdapter? = null
    private var mRecyclerView: MyRecyclerView? = null
    override val layoutId = R.layout.owner_results_fragment
    override val viewModelClass = OwnerResultsViewModel::class.java
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

    override fun onViewCreated(view: View, viewModel: OwnerResultsViewModel, instance: Bundle?) {


        mViewModel.ownerId.value = arguments?.getString(OWNER_KEY)

        viewModel.searchPhotos(R.id.ownerRV)

        mAdapter = OwnerAdapter(viewModel)

        mRecyclerView = createRecyclerView(mAdapter!!, recyclerViewId = R.id.ownerRV)
    }

}