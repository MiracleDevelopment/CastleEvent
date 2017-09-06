package com.ipati.dev.castleevent.service

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ipati.dev.castleevent.ListEventActivity
import com.ipati.dev.castleevent.model.userManage.photoUrl
import com.ipati.dev.castleevent.model.userManage.uid
import com.ipati.dev.castleevent.model.userManage.userEmail
import com.ipati.dev.castleevent.model.userManage.username

class LoginAuthManager(context: Context, lifecycle: Lifecycle) : LifecycleObserver {
    private var mContext: Context = context
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mLifecycle: Lifecycle = lifecycle
    private lateinit var fireBaseUser: FirebaseUser
    private lateinit var listEventIntent: Intent

    init {
        mLifecycle.addObserver(this)
    }

    fun loginAuthentication(email: String, password: String, usernameView: EditText, passwordView: EditText) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fireBaseUser = FirebaseAuth.getInstance().currentUser!!
                uid = fireBaseUser.uid
                username = fireBaseUser.displayName.toString()
                userEmail = fireBaseUser.email.toString()
                photoUrl = fireBaseUser.photoUrl.toString()

                listEventIntent = Intent(mContext, ListEventActivity::class.java)
                mContext.startActivity(listEventIntent)

                usernameView.setText("")
                passwordView.setText("")

                Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(mContext, exception.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}

