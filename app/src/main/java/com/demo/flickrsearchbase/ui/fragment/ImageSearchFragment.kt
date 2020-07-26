package com.demo.flickrsearchbase.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.demo.flickrsearchbase.R
import com.demo.flickrsearchbase.ui.viewmodel.SearchViewModel
import com.demo.flickrsearchbase.utils.Adapter
import com.demo.flickrsearchbase.utils.OWNER_KEY
import com.subwilven.basemodel.project_base.base.fragments.BaseSuperFragment
import com.subwilven.basemodel.project_base.views.MyRecyclerView
import kotlinx.android.synthetic.main.image_search_fragment.*

class ImageSearchFragment : BaseSuperFragment<SearchViewModel>() {

    override val fragmentTag = "imgSearch"
    private var mAdapter: Adapter? = null
    private var mRecyclerView: MyRecyclerView? = null
    override val layoutId = R.layout.image_search_fragment
    override val viewModelClass = SearchViewModel::class.java
    override val viewModelLifecycle = this

    override fun setUpObservers() {
        mViewModel.photosLiveList.observe(viewLifecycleOwner, Observer { photos ->
            mAdapter?.addItems(photos)
        })

        mViewModel.ownerId.observe(viewLifecycleOwner, Observer {
            val bundle = Bundle()
            bundle.putString(OWNER_KEY, mViewModel.ownerId.value)
            navigate(OwnerFragment(), bundle)
        })
    }

    override fun onViewCreated(view: View, viewModel: SearchViewModel, instance: Bundle?) {

        searchBtn.setOnClickListener {
            mViewModel.queryText.value = keywordTxt.text.toString()
            viewModel.searchPhotos(R.id.searchBtn)
        }
        addSensitiveInputs(searchRV, keywordTxt)

        mAdapter = Adapter(viewModel)

        mRecyclerView = createRecyclerView(mAdapter!!, recyclerViewId = R.id.searchRV)
    }


}