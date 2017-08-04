package com.ipati.dev.castleevent.fragment.loading

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import kotlinx.android.synthetic.main.activity_loading_fragment.*

class LoadingFragment : Fragment() {
    var bundle: Bundle? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_loading_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (bundle != null) {
            val status: Boolean = bundle?.getBoolean(loadingFragmentBundle, false)!!
            if (status) {
                loading_view.show()
            } else {
                loading_view.hide()
            }
        }
    }

    companion object {
        var loadingFragmentBundle: String = "LoadingFragment"
        fun newInstance(status: Boolean): LoadingFragment {
            val loadingFragment: LoadingFragment = LoadingFragment()
            val bundle: Bundle = Bundle()
            bundle.putBoolean(loadingFragmentBundle, status)
            loadingFragment.arguments = bundle
            return loadingFragment
        }
    }
}
