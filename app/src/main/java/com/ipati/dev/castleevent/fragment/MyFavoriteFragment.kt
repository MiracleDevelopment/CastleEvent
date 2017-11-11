package com.ipati.dev.castleevent.fragment

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ipati.dev.castleevent.FavoriteCategoryActivity
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.base.BaseFragment
import com.ipati.dev.castleevent.extension.onDismissDialog
import com.ipati.dev.castleevent.extension.onShowLoadingDialog
import com.ipati.dev.castleevent.model.UserManager.uid
import com.ipati.dev.castleevent.service.FirebaseService.FavoriteCategoryRealTimeDatabaseManager
import kotlinx.android.synthetic.main.activity_my_favorite_fragment.*
import kotlin.collections.HashMap

class MyFavoriteFragment : BaseFragment() {
    private lateinit var favoriteCategoryManager: FavoriteCategoryRealTimeDatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_my_favorite_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteCategoryManager = FavoriteCategoryRealTimeDatabaseManager(lifecycle)
        setUpToolbar()
        setUpRecyclerView()

        tv_add_category.setOnClickListener {
            setUpAddCategory()
        }
    }

    private fun setUpAddCategory() {
        val intentAddCategory = Intent(context, FavoriteCategoryActivity::class.java)
        intentAddCategory.putExtra(objectStatus, false)
        startActivity(intentAddCategory)
    }

    private fun setUpToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar_favorite)
            supportActionBar?.apply {
                title = ""
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }
    }

    private fun setUpRecyclerView() {
        recycler_view_my_favorite.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        recycler_view_my_favorite.itemAnimator = DefaultItemAnimator()
        recycler_view_my_favorite.adapter = favoriteCategoryManager.adapterFavorite

        favoriteCategoryManager.adapterFavorite.setOnRemoveItem = {
            val ref = FirebaseDatabase.getInstance().reference
            val refEdit = ref.child("userCategoryProfile").child(uid)
            val childrenChangeData: HashMap<String, Any> = HashMap()

            refEdit.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    Log.d("onCancelledUpdateChild", p0?.message.toString())
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    val loadingDialogFragment = onShowLoadingDialog(activity, "ระบบกำลังลบข้อมูล...", false)
                    for (item in p0?.children!!) {
                        childrenChangeData.put("${item.key}/listCategory"
                                , favoriteCategoryManager.adapterFavorite.listItemFavoriteCategory[0].listCategory)

                        refEdit.updateChildren(childrenChangeData).addOnCompleteListener { task: Task<Void> ->
                            when {
                                task.isSuccessful -> {
                                    loadingDialogFragment.onDismissDialog()
                                }
                                else -> {
                                    loadingDialogFragment.onDismissDialog()
                                }
                            }
                        }
                    }
                }
            })
        }

        favoriteCategoryManager.onChangeItemCount = { count ->
            view?.let {
                when (count) {
                    0 -> {
                        recycler_view_my_favorite.visibility = View.GONE
                        tv_no_item.visibility = View.VISIBLE
                        tv_add_category.visibility = View.VISIBLE
                    }

                    else -> {
                        recycler_view_my_favorite.visibility = View.VISIBLE
                        tv_no_item.visibility = View.GONE
                        tv_add_category.visibility = View.GONE
                    }
                }
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
        fun newInstance(): MyFavoriteFragment = MyFavoriteFragment().apply { arguments = Bundle() }
    }
}
