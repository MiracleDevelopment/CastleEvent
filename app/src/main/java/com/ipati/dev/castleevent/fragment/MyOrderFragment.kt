package com.ipati.dev.castleevent.fragment


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.firebase.database.*
import com.ipati.dev.castleevent.base.BaseFragment
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.extension.*
import com.ipati.dev.castleevent.fragment.loading.LoadingDialogFragment
import com.ipati.dev.castleevent.fragment.loading.TicketsEventDialogFragment
import com.ipati.dev.castleevent.model.History.RecorderTickets
import com.ipati.dev.castleevent.model.UserManager.uid
import com.ipati.dev.castleevent.service.FirebaseService.MyOrderRealTimeManager
import kotlinx.android.synthetic.main.activity_my_order_fragment.*

class MyOrderFragment : BaseFragment() {
    private val myOrderRealTimeManager: MyOrderRealTimeManager by lazy {
        MyOrderRealTimeManager(context, lifecycle)
    }

    private lateinit var ticketsEventDialog: TicketsEventDialogFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.activity_my_order_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialToolbar()
        initialRecyclerView()
    }

    private fun initialToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar_my_order)
            title = ""
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }
    }

    fun onShowTicketsUserDialog(eventId: String, eventPhoto: String?, eventName: String, userAccount: String, eventLocation: String, count: Long) {
        ticketsEventDialog = TicketsEventDialogFragment.newInstance(eventId
                , eventPhoto, eventName
                , userAccount
                , eventLocation, count)

        ticketsEventDialog.isCancelable = false
        ticketsEventDialog.show(activity.supportFragmentManager, "TicketsDialog")
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity.supportFinishAfterTransition()
            }
        }
        return true
    }

    private fun initialRecyclerView() {
        recycler_view_my_order.apply {
            layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
            adapter = myOrderRealTimeManager.adapterMyOrder
        }

        myOrderRealTimeManager.adapterMyOrder.callBackItemCount = {
            when (it) {
                0 -> {
                    tv_no_item_tickets.visibility = View.VISIBLE
                }
                else -> {
                    tv_no_item_tickets.visibility = View.GONE
                }
            }
        }


        myOrderRealTimeManager.adapterMyOrder.callBackLongPress = { eventPushId: String, _: String, position: Int ->
            setUpCallBack(eventPushId, position)
        }
    }

    private fun setUpCallBack(eventPushId: String, position: Int) {
        onShowQuestionDialog(activity, "คุณต้องการลบอีเว้นนี้ ใช่ / ไม่", 1003).callBackMyOrderQuestion = {
            deleteDataFireBase(eventPushId, position)
        }
    }

    private fun deleteDataFireBase(eventPushId: String, position: Int) {
        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference
        val refMyOder: DatabaseReference = ref.child("eventUser").child(uid)
        val loadingDialogFragment = onShowLoadingDialog(activity, "ระบบกำลังลบข้อมูล...", false)
        refMyOder.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                context.onShowToast(p0?.message?.toString()!!)
            }

            override fun onDataChange(p0: DataSnapshot?) {
                for (item in p0?.children!!) {
                    val itemEvent = item.getValue(RecorderTickets::class.java)
                    when (itemEvent?.eventTickets) {
                        eventPushId -> {
                            val refChildMyOrder = ref.child("eventUser")
                            val mapData: HashMap<String, Any?> = HashMap()
                            mapData.put("$uid/${item.key}", null)
                            callBackFireBase(refChildMyOrder, mapData, position, loadingDialogFragment)
                        }
                    }
                }
            }
        })
    }

    private fun callBackFireBase(ref: DatabaseReference, mapData: HashMap<String, Any?>, position: Int, loadingDialogFragment: LoadingDialogFragment) {
        ref.updateChildren(mapData).addOnCompleteListener {
            when {
                it.isSuccessful -> {
                    myOrderRealTimeManager.adapterMyOrder.listItemMyOrder.removeAt(position)
                    myOrderRealTimeManager.adapterMyOrder.notifyItemRemoved(position)
                    loadingDialogFragment.onDismissDialog()
                }
                else -> {
                    Log.d("onCancelChangeData",it.exception?.message.toString())
                    loadingDialogFragment.onDismissDialog()
                }
            }
        }
    }

    companion object {
        fun newInstance(): MyOrderFragment = MyOrderFragment().apply {
            arguments = Bundle()
        }
    }
}
