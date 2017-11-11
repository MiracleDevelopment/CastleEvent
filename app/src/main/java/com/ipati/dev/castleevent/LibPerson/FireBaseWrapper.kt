package com.ipati.dev.castleevent.LibPerson

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import java.lang.Exception

class FireBaseWrapper : OnCompleteListener<Void>, OnFailureListener {
    private var fireBaseTaskList: List<Task<Void>>? = null
    private var taskWork: Int = 0

    var addCompleteSuccess: (() -> Unit)? = null
    var addOnFailure: ((errorMessage: Exception) -> Unit)? = null

    constructor(fireBaseTask: List<Task<Void>>) : super() {
        fireBaseTaskList = fireBaseTask
        setFireBaseProcess()
    }

    constructor(fireBaseTask: Task<Void>) : super()

    private fun taskSize(): Int {
        return fireBaseTaskList!!.size
    }

    private fun setFireBaseProcess() {
        fireBaseTaskList?.let {
            for (fireBase in fireBaseTaskList!!) {
                fireBase.addOnCompleteListener(this)
                fireBase.addOnFailureListener(this)
            }
        }
    }

    override fun onComplete(p0: Task<Void>) {
        taskWork++
        when (taskWork) {
            taskSize() -> {
                taskWork = taskSize() - taskSize()
                addCompleteSuccess?.invoke()
            }
        }
    }

    override fun onFailure(p0: Exception) {
        when (taskWork) {
            in 0..taskSize() -> {
                taskWork--
            }
        }
        addOnFailure?.invoke(p0)
    }
}