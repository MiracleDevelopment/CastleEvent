package com.ipati.dev.castleevent.service

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ipati.dev.castleevent.ListEventActivity
import com.ipati.dev.castleevent.extension.onShowDialog
import com.ipati.dev.castleevent.fragment.loading.LoadingDialogFragment
import com.ipati.dev.castleevent.model.userManage.photoUrl
import com.ipati.dev.castleevent.model.userManage.uid
import com.ipati.dev.castleevent.model.userManage.userEmail
import com.ipati.dev.castleevent.model.userManage.username

class LoginAuthManager(activity: FragmentActivity, lifecycle: Lifecycle) : LifecycleObserver {
    private var activity: FragmentActivity? = null
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mLifecycle: Lifecycle = lifecycle
    private lateinit var fireBaseUser: FirebaseUser
    private lateinit var listEventIntent: Intent

    init {
        this.activity = activity
        mLifecycle.addObserver(this)
    }

    fun loginAuthentication(email: String, password: String, usernameView: EditText, passwordView: EditText) {
        val loadingDialogFragment: LoadingDialogFragment = LoadingDialogFragment.newInstance("ระบบกำลังล๊อคอิน...", false)
        loadingDialogFragment.onShowDialog(activity!!)

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fireBaseUser = FirebaseAuth.getInstance().currentUser!!
                uid = fireBaseUser.uid
                username = fireBaseUser.displayName.toString()
                userEmail = fireBaseUser.email.toString()
                photoUrl = fireBaseUser.photoUrl.toString()

                listEventIntent = Intent(activity, ListEventActivity::class.java)
                activity!!.startActivity(listEventIntent)

                usernameView.setText("")
                passwordView.setText("")

                loadingDialogFragment.dismiss()
            }
        }.addOnFailureListener { exception ->
            loadingDialogFragment.dismiss()
            Toast.makeText(activity, exception.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}

