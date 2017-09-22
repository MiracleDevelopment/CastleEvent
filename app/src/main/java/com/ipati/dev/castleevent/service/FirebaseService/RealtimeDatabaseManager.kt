package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.util.Log
import com.google.firebase.database.*
import com.ipati.dev.castleevent.adapter.ListCategoryMenuAdapter
import com.ipati.dev.castleevent.adapter.ListEventAdapter
import com.ipati.dev.castleevent.model.category.ALL
import com.ipati.dev.castleevent.model.category.Education
import com.ipati.dev.castleevent.model.category.Sport
import com.ipati.dev.castleevent.model.category.Technology
import com.ipati.dev.castleevent.model.modelListEvent.ItemListEvent

class RealTimeDatabaseManager(context: Context, lifeCycle: Lifecycle) : LifecycleObserver {
    private var tagChild = "eventItem/eventContinue"
    private var refDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var refDatabaseChild: DatabaseReference = refDatabase.child(tagChild)
    var listItem: ItemListEvent? = null
    var hasMapData: HashMap<*, *>? = null
    var mContext: Context? = null
    var mLifeCycle: Lifecycle? = null
    var arrayItemList: ArrayList<ItemListEvent> = ArrayList()
    var mListCategory: ArrayList<String> = ArrayList()
    var adapterListEvent: ListEventAdapter? = ListEventAdapter(listItem = arrayItemList)
        get() = field
    var adapterCategory: ListCategoryMenuAdapter = ListCategoryMenuAdapter(mListCategory)
        get() = field
    var mCategory: String = "ALL"
    lateinit var listItemEvent: List<ItemListEvent>

    //Todo:init Class Constructor
    init {
        this.mContext = context
        this.mLifeCycle = lifeCycle
        mLifeCycle!!.addObserver(this)
    }

    //Todo:Handling Life Cycle
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        mListCategory.apply {
            add("ALL")
            add("Education")
            add("Technology")
            add("Sport")
        }

        adapterCategory.notifyDataSetChanged()
        registerChildEvent()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        mListCategory.clear()
        removeChildEvent()
        removeValueCategory()
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mLifeCycle?.removeObserver(this)
    }

    private fun registerChildEvent() {
        refDatabaseChild.addChildEventListener(childEventListener())
    }

    private fun removeChildEvent() {
        refDatabaseChild.removeEventListener(childEventListener())
        arrayItemList.clear()
    }

    fun onChangeCategory() {
        removeValueCategory()
        arrayItemList.clear()
        refDatabaseChild.addChildEventListener(childEventListener())
    }

    private fun childEventListener(): ChildEventListener {
        return object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.d("onCancelled", p0?.message.toString())
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                hasMapData = p0?.value as HashMap<*, *>
                val objectItem: List<ItemListEvent> = arrayItemList.filter { it.eventId == hasMapData!!["eventId"] as Long }
                if (objectItem.count() != 0) {
                    listItem = ItemListEvent(
                            p0.key.toString(),
                            hasMapData!!["eventId"] as Long,
                            hasMapData!!["eventName"].toString(),
                            hasMapData!!["eventCover"].toString(),
                            hasMapData!!["eventAdvertise"].toString(),
                            hasMapData!!["categoryName"].toString(),
                            hasMapData!!["accountBank"].toString(),
                            hasMapData!!["eventDescription"].toString(),
                            hasMapData!!["eventLocation"].toString(),
                            hasMapData!!["eventLogoCredit"].toString(),
                            hasMapData!!["eventLatitude"] as Double,
                            hasMapData!!["eventLongitude"] as Double,
                            hasMapData!!["eventMax"] as Long,
                            hasMapData!!["eventRest"] as Long,
                            hasMapData!!["eventStatus"] as Boolean,
                            hasMapData!!["eventTime"].toString(),
                            hasMapData!!["eventCalendarStart"].toString(),
                            hasMapData!!["eventCalendarEnd"].toString(),
                            hasMapData!!["eventPrice"].toString()
                    )
                    arrayItemList.remove(objectItem[0])
                    adapterListEvent?.notifyDataSetChanged()
                    arrayItemList.add(0, listItem!!)
                }
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                hasMapData = p0?.value as HashMap<*, *>
                listItem = ItemListEvent(
                        p0.key.toString(),
                        hasMapData!!["eventId"] as Long,
                        hasMapData!!["eventName"].toString(),
                        hasMapData!!["eventCover"].toString(),
                        hasMapData!!["eventAdvertise"].toString(),
                        hasMapData!!["categoryName"].toString(),
                        hasMapData!!["accountBank"].toString(),
                        hasMapData!!["eventDescription"].toString(),
                        hasMapData!!["eventLocation"].toString(),
                        hasMapData!!["eventLogoCredit"].toString(),
                        hasMapData!!["eventLatitude"] as Double,
                        hasMapData!!["eventLongitude"] as Double,
                        hasMapData!!["eventMax"] as Long,
                        hasMapData!!["eventRest"] as Long,
                        hasMapData!!["eventStatus"] as Boolean,
                        hasMapData!!["eventTime"].toString(),
                        hasMapData!!["eventCalendarStart"].toString(),
                        hasMapData!!["eventCalendarEnd"].toString(),
                        hasMapData!!["eventPrice"].toString()

                )
                onCountItemCategory(listItem!!)

                if (mCategory != "ALL") {
                    if (listItem?.categoryName == mCategory) {
                        arrayItemList.add(listItem!!)
                        adapterListEvent?.notifyDataSetChanged()
                    }

                } else if (mCategory == "ALL") {
                    arrayItemList.add(listItem!!)
                    adapterListEvent?.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                adapterListEvent?.notifyDataSetChanged()
                hasMapData = p0?.value as HashMap<*, *>
                listItemEvent = arrayItemList.filter { it.eventId == hasMapData!!["eventId"] as Long }

                if (listItemEvent.count() != 0) {
                    arrayItemList.remove(listItemEvent[0])
                }
            }
        }
    }


    fun onCountItemCategory(listItem: ItemListEvent) {
        ALL += 1

        when {
            listItem.categoryName == "Education" -> Education += 1
            listItem.categoryName == "Technology" -> Technology += 1
            else -> Sport += 1
        }
    }

    private fun removeValueCategory() {
        ALL = 0
        Education = 0
        Sport = 0
        Technology = 0
    }


}
