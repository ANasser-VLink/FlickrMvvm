package com.demo

import Adapter
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.demo.image_search.R
import com.subwilven.basemodel.project_base.base.fragments.BaseSuperFragment
import com.subwilven.basemodel.project_base.views.MyRecyclerView
import kotlinx.android.synthetic.main.search_fragment.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SearchFragment : BaseSuperFragment<SearchViewModel>() {

    override val fragmentTag = "imgSearch"
    private var mAdapter: Adapter? = null
    private var mRecyclerView: MyRecyclerView? = null
    override val layoutId = R.layout.search_fragment
    override val viewModelClass = SearchViewModel::class.java
    override val viewModelLifecycle = this
    private val listener: NavigationListener by inject {
        parametersOf(activity?.supportFragmentManager)
    }


    override fun setUpObservers() {
        mViewModel.photosLiveList.observe(viewLifecycleOwner, Observer { photos ->
            mAdapter?.addItems(photos)
        })

        mViewModel.ownerId.observe(viewLifecycleOwner, Observer {
            listener.onBtnClicked(mViewModel.ownerId.value!!)

//            searchRV.findNavController().navigate(action)
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