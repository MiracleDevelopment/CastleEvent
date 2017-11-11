package com.ipati.dev.castleevent.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.adapter.FavoriteCategoryAdapter
import com.ipati.dev.castleevent.extension.onDismissDialog
import com.ipati.dev.castleevent.extension.onShowLoadingDialog
import com.ipati.dev.castleevent.model.UserManager.gender
import com.ipati.dev.castleevent.model.UserManager.uid
import com.ipati.dev.castleevent.model.UserManager.uidRegister
import com.ipati.dev.castleevent.service.RecordedEvent.RecordCategory
import kotlinx.android.synthetic.main.activity_favorite_category_fragment.*
import kotlinx.android.synthetic.main.custom_layout_favorite_category.view.*
import kotlinx.android.synthetic.main.layout_button_record_category.*

class FavoriteCategoryFragment : Fragment() {
    private val adapterCategory: FavoriteCategoryAdapter by lazy {
        FavoriteCategoryAdapter(context)
    }

    private lateinit var itemSelectCategory: ArrayList<Int>
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_favorite_category_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setUpToolbar()
        setUpRecyclerView()
        setUpView()

    }

    private fun setUpToolbar() {
        (activity as AppCompatActivity).apply {
            title = ""
            setSupportActionBar(toolbar_favorite_category)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setUpView() {
        tv_success_category.setOnClickListener {
            if (itemSelectCategory.count() > 1) {
                if (!arguments.getBoolean(objectStatus)) {
                    updateCategory(uid!!)
                } else {
                    updateCategory(uidRegister!!)
                }
            } else {
                Toast.makeText(context, "Please Select More 1 Item", Toast.LENGTH_SHORT).show()
            }
        }

        tv_skip_category.setOnClickListener {
            activity.supportFinishAfterTransition()
        }
    }

    private fun updateCategory(uidSwitch: String) {
        val loadingDialogFragment = onShowLoadingDialog(activity, "ระบบกำลังบันทึกข้อมูล...", false)
        val recordCategory = RecordCategory()
        recordCategory.pushProfile(uidSwitch, itemSelectCategory)?.addOnCompleteListener { task: Task<Void> ->
            when {
                task.isSuccessful -> {
                    loadingDialogFragment.onDismissDialog()
                    activity.supportFinishAfterTransition()
                }
                else -> {
                    loadingDialogFragment.onDismissDialog()
                    Log.d("errorSuccessCategory", task.exception?.message.toString())
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        itemSelectCategory = ArrayList()
        recycler_favorite_category.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        recycler_favorite_category.itemAnimator = DefaultItemAnimator()
        recycler_favorite_category.adapter = adapterCategory

        adapterCategory.onSelectorItemCategory = { view, position ->
            view.isActivated = !view.isActivated
            if (view.tv_item_category_favorite.isActivated) {
                view.tv_item_category_favorite.setTextColor(ContextCompat.getColor(view.context, android.R.color.black))
                itemSelectCategory.add(position)
            } else {
                itemSelectCategory.remove(position)
                view.tv_item_category_favorite.setTextColor(ContextCompat.getColor(view.context, android.R.color.white))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity.supportFinishAfterTransition()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val objectStatus = "status"
        fun newInstance(statusCheckUpload: Boolean): FavoriteCategoryFragment = FavoriteCategoryFragment().apply {
            arguments = Bundle().apply {
                putBoolean(objectStatus, statusCheckUpload)
            }
        }
    }
}
