package com.demo.flickrsearchbase.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.demo.flickrsearchbase.R
import com.demo.flickrsearchbase.data.Repo
import com.demo.flickrsearchbase.pojo.Photo
import com.demo.flickrsearchbase.ui.viewmodel.ImageSearchViewModel
import com.demo.flickrsearchbase.ui.viewmodel.OwnerViewModel
import com.demo.flickrsearchbase.utils.Adapter
import com.demo.flickrsearchbase.utils.OWNER_KEY
import com.demo.flickrsearchbase.utils.OwnerAdapter
import com.demo.flickrsearchbase.utils.PER_PAGE
import com.subwilven.basemodel.project_base.base.fragments.BaseFragment
import com.subwilven.basemodel.project_base.base.fragments.BaseSuperFragment
import com.subwilven.basemodel.project_base.base.other.SingleLiveEvent
import com.subwilven.basemodel.project_base.views.MyRecyclerView
import kotlinx.android.synthetic.main.image_search_fragment.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        mViewModel.ownerId.value = arguments?.getString(OWNER_KEY)

    }

    override fun onViewCreated(view: View, viewModel: OwnerViewModel, instance: Bundle?) {


        viewModel.searchPhotos(R.id.searchBtn)

        mAdapter = OwnerAdapter(viewModel)

        mRecyclerView = createRecyclerView(mAdapter!!, recyclerViewId = R.id.searchRV)
    }

}