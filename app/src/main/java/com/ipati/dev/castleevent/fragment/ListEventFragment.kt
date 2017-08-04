package com.ipati.dev.castleevent.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R

class ListEventFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_list_event_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        var listEventObject: String = "ListEventFragment"
        fun newInstance(nameObject: String): ListEventFragment {
            val listEventFragment: ListEventFragment = ListEventFragment()
            val bundle: Bundle = Bundle()
            bundle.putString(listEventObject, nameObject)
            listEventFragment.arguments = bundle
            return listEventFragment
        }
    }
}
