package com.subwilven.basemodel.project_base.base.fragments

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager

import com.subwilven.basemodel.R
import com.subwilven.basemodel.project_base.base.activities.BaseActivity
import com.subwilven.basemodel.project_base.base.other.BaseViewModel
import com.subwilven.basemodel.project_base.utils.DialogManager
import com.subwilven.basemodel.project_base.utils.ImagePicker
import com.subwilven.basemodel.project_base.utils.LocationUtils
import com.subwilven.basemodel.project_base.utils.PermissionsManager
import com.subwilven.basemodel.project_base.utils.extentions.showToast
import com.subwilven.basemodel.project_base.utils.extentions.showToastLong
import com.subwilven.basemodel.project_base.views.OnViewStatusChange
import java.io.File


public abstract class BaseFragment<V : BaseViewModel> : Fragment(), DialogManager {


    lateinit var mViewModel: V

    private var mViewRoot: View? = null
    abstract val fragmentTag: String
    private var baseActivity: BaseActivity? = null
    private var mLoadingView: View? = null
    private var mErrorView: View? = null
    private var savedInstanceState: Bundle? = null
    private val sensitiveInputViews = mutableListOf<View?>()
    open var handleOnBackPressed = false


    //used to spicify this fragment should observe screen status or its children will take this responsibility
    open var hasChildrenFragments = false

    open var enableBackButton = false
    open var toolbarTitle = -1
    open var optionMenuId = -1
    abstract val layoutId :Int
    abstract val viewModelLifecycle :ViewModelStoreOwner
    abstract val viewModelClass :Class<V>


    //override this method if you need to indetif another view group if the
    // loading full screen overlap on another view
    open val fullScreenViewGroup: ViewGroup
        get() = mViewRoot as ViewGroup

    val isNetworkConnected: Boolean
        get() = baseActivity != null && baseActivity!!.isNetworkConnected

    override val _fragmentManager: FragmentManager?
        get() = childFragmentManager
    override val _context: Context
        get() = context!!

    protected abstract fun onViewCreated(view: View, viewModel: V, instance: Bundle?)

    protected abstract fun setUpObservers()

    open fun onRetry() {
        mViewModel.loadInitialData()
    }

    protected fun addSensitiveInputs(vararg views: View?) {
        for (view in views) {
            sensitiveInputViews.add(view)
        }
    }

    protected fun enableSensitiveInputs(shouldEnable: Boolean) {
        for (view in sensitiveInputViews)
            view?.isEnabled = shouldEnable
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            this.baseActivity = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkValidResources()

        if (optionMenuId != -1)
            setHasOptionsMenu(true)
    }

    protected fun markScreenAsCompleted() {
        mViewModel.markAsCompleted(fragmentTag)
    }

    private fun checkValidResources() {
        if (layoutId == -1)
            throw IllegalArgumentException("you should call initContentView() method inside onLaunch Callback")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.mViewModel = ViewModelProvider(viewModelLifecycle)[viewModelClass]

        this.savedInstanceState = savedInstanceState
        mViewRoot = inflater.inflate(layoutId, container, false)

        //register fragment so we can determine should we show full screen loading by consume screen status
        mViewModel.registerFragment(fragmentTag)

        // 1- first condition to prevent load main data in confirgation change
        // 2- second condition if the viewmodel still alive (getActivity lifeclceOwner) this flag to prevent
        // loading the main data because the first condition not hep in this case or  the fragment launched from backstack
        // 3-the theird condition helpfull fragment has children so their childs dont load the main data of the parent fragment agian
        if ((savedInstanceState == null && mViewModel.isNewInstant) && parentFragment == null) {
            mViewModel.loadInitialData()
            mViewModel.isNewInstant = false
        }

        return mViewRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isFocusableInTouchMode = true
        view.requestFocus()

        //only apply for the parent fragment
        if (parentFragment == null) {
            setUpToolbar()
            observeDefaults()
        }

        //used to spicify this fragment should observe screen status or its children will take this responsibility
        if (!hasChildrenFragments)
            observeScreenStatus()

        onViewCreated(view, mViewModel, savedInstanceState)
        setUpObservers()
    }


    private fun observeDefaults() {

        mViewModel.mDialogMessage.observe(viewLifecycleOwner, Observer {
            showDialog(R.string.ibase_important, it)
        })

        //TODO need to be implemented
        mViewModel.mSnackBarMessage.observe(viewLifecycleOwner, Observer {})

        mViewModel.mDialogMessage.observe(viewLifecycleOwner, Observer {
            showDialog(R.string.title1, it)
        })

        mViewModel.mToastMessage.observe(
            viewLifecycleOwner,
            Observer { context?.showToastLong(it) })

        mViewModel.mEnableSensitiveInputs.observe(
            viewLifecycleOwner,
            Observer { enableSensitiveInputs(it) })

    }

    protected fun observeScreenStatus() {

        mViewModel.mShowLoadingFullScreen?.observe(viewLifecycleOwner, Observer {
            if (it.first != fragmentTag) return@Observer
            if (it.second) {
                inflateLoadingFullScreenView()
                show(mLoadingView)
            } else {
                mLoadingView?.let {
                    val transition: Transition = Fade()
                    transition.duration = 350
                    transition.addTarget(mLoadingView!!)
                    TransitionManager.beginDelayedTransition(mViewRoot as ViewGroup, transition)
                    hide(mLoadingView)
                }
            }
        })

        mViewModel.mShowErrorFullScreen?.observe(viewLifecycleOwner, Observer {
            if (it.first != fragmentTag) return@Observer
            if (it.second != null) {
                inflateErrorFullScreenView(it.second!!)
                show(mErrorView)
            } else
                hide(mErrorView)
        })

        //TODO a better way to do this without needing to find view by id through all the list ( set list of this view in the fragment)
        mViewModel.mLoadingViews.observe(viewLifecycleOwner, Observer {
           // if (it.first != fragmentTag) return@Observer
            for (viewId in it.second)
                (mViewRoot?.findViewById<View>(viewId.key) as? OnViewStatusChange)?.showLoading(
                    viewId.value
                )
        })
    }

    //set fragment root view to be the dialog view so when searching for loading views we can find it
    fun exchangeRootViewToDialogView(view: View) {
        mViewRoot = view
    }

    //set fragment root view to be the dialog view so when searching for loading views we can find it
    fun exchangeRootViewToFragmentView() {
        mViewRoot = view
    }

    private fun inflateLoadingFullScreenView() {
        //   baseActivity!!.runOnUiThread {
        val viewGroup = fullScreenViewGroup
        if (mLoadingView != null) return
        mLoadingView = LayoutInflater.from(context).inflate(
            R.layout.ibase_layout_progress_bar,
            viewGroup, false
        )
        viewGroup.addView(mLoadingView)
        // }
    }

    private fun inflateErrorFullScreenView(errorModel: com.subwilven.basemodel.project_base.POJO.ErrorModel) {
        val viewGroup = fullScreenViewGroup
        if (mErrorView == null) {
            mErrorView = LayoutInflater.from(context).inflate(
                R.layout.ibase_layout_no_connection,
                viewGroup, false
            )

            //to handel onRetry in each fragment individually
            mErrorView!!.findViewById<View>(R.id.btn_retry).setOnClickListener {
                if (isNetworkConnected)
                    onRetry()
            }

            viewGroup.addView(mErrorView)
        }
        (mErrorView!!.findViewById<View>(R.id.tv_title) as TextView).text =
            errorModel.title.getValue(context)
        (mErrorView!!.findViewById<View>(R.id.tv_message) as TextView).text =
            errorModel.message.getValue(context)
    }


    override fun onDestroyView() {
        if (mViewModel != null)
            mViewModel.unRegister(fragmentTag)
        sensitiveInputViews.clear()
        mViewRoot = null
        mLoadingView = null
        mErrorView = null
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(optionMenuId, menu)
    }

    override fun onDetach() {
        baseActivity = null
        mViewRoot = null
        super.onDetach()
    }

    protected fun finish() {
        activity?.finish()
    }

    fun setUpToolbar() {
        if (toolbarTitle != -1)
            baseActivity!!.setToolbarTitle(toolbarTitle)
        baseActivity?.enableBackButton(enableBackButton)
    }

    protected fun setToolbarTitle(title :String){
        baseActivity!!.setToolbarTitle(title)
    }

    fun pickImage( onFailed :(()->Unit)? =null,  onImagePicked: (imageFile: File?) -> Unit) {
        ImagePicker.pickImage(this,onFailed, onImagePicked)
    }

    fun hide(vararg views: View?) {
        baseActivity?.hide(*views)
    }

    fun show(vararg views: View?) {
        baseActivity?.show(*views)
    }


    fun getUserLocationSingle(
        priority: Int = LocationUtils.DEFAULT_PRIORITY,
        onFailed: (() -> Unit) = {},
        onLocation: (location: Location) -> Unit
    ) {
        LocationUtils.instance?.getUserLocationSingle(this, priority, onFailed, onLocation)
    }

    fun getUserLocationUpdates(
        priority: Int = LocationUtils.DEFAULT_PRIORITY,
        interval: Long = LocationUtils.DEFAULT_INTERVAL,
        fastestInterval: Long = LocationUtils.DEFAULT_FASTEST_INTERVAL,
        onFailed: (() -> Unit) = {},
        onLocation: (location: Location) -> Unit
    ) {
        LocationUtils.instance?.getUserLocationUpdates(
            this,
            priority,
            interval,
            fastestInterval,
            onFailed,
            onLocation
        )
    }

    fun showDialog(dialog: DialogFragment) {
        dialog.show(childFragmentManager, "dfdf")
    }

    fun toast(msg: String, lenght: Int = Toast.LENGTH_LONG) {
        context?.showToast(msg, lenght)
    }

    fun navigate(cls: Class<*>, bundle: Bundle? = null, clearBackStack: Boolean = false) {
        baseActivity?.navigate(cls, bundle, clearBackStack)
    }

    fun navigate(
        fragment: BaseFragment<*>, bundle: Bundle? = null,
        @IdRes container: Int = R.id.container,
        addToBackStack: Boolean = false,
        isChildToThisFragment: Boolean = false
    ) {
        val fragmentManager =
            if (isChildToThisFragment) childFragmentManager else activity?.supportFragmentManager
        baseActivity?.navigate(fragmentManager!!, fragment, bundle, container, addToBackStack)
    }

    fun requestPermission(
        vararg permissions: String,
        onDenied: (() -> Unit)? = null,
        onGranted: (() -> Unit)? = null
    ) {
        PermissionsManager.requestPermission(
            this,
            *permissions,
            onGranted = onGranted,
            onDenied = onDenied
        )
    }

    open fun onBackPressed() {
        activity?.onBackPressed()
    }

}


