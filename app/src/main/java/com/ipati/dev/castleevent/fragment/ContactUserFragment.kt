package com.ipati.dev.castleevent.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import kotlinx.android.synthetic.main.activity_contact_user_fragment.*

class ContactUserFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_contact_user_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialToolbar()
    }

    private fun initialToolbar() {
        (activity as AppCompatActivity).apply {
            title = ""
            setSupportActionBar(toolbar_contact).apply {
                supportActionBar?.apply {
                    setDisplayShowHomeEnabled(true)
                    setDisplayHomeAsUpEnabled(true)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity.finish()
            }
        }
        return true
    }

    companion object {
        fun newInstance(): ContactUserFragment {
            return ContactUserFragment().apply {
                arguments = Bundle()
            }
        }
    }
}
