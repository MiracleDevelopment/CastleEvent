package com.ipati.dev.castleevent.service

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ipati.dev.castleevent.ListEventActivity
import com.ipati.dev.castleevent.extension.onShowDialog
import com.ipati.dev.castleevent.fragment.loading.LoadingDialogFragment
import com.ipati.dev.castleevent.model.UserManager.photoUrl
import com.ipati.dev.castleevent.model.UserManager.uid
import com.ipati.dev.castleevent.model.UserManager.userEmail
import com.ipati.dev.castleevent.model.UserManager.username

class LoginAuthManager(activity: FragmentActivity, lifecycle: Lifecycle) : LifecycleObserver {
    private var activity: FragmentActivity? = null
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mLifecycle: Lifecycle = lifecycle
    private lateinit var fireBaseUser: FirebaseUser
    private lateinit var listEventIntent: Intent
    var callBackSuccessValidate: (() -> Unit?)? = null
    var callBackFailureValidate: ((msg: String) -> Unit?)? = null

    init {
        this.activity = activity
        mLifecycle.addObserver(this)
    }

    fun stateValidate(emailEditText: EditText, passwordEditText: EditText, onSuccess: (() -> Unit)
                      , onErrorEmail: ((errorMsg: String) -> Unit), onErrorPassword: ((errorPassword: String) -> Unit)) {
        when {
            emailEditText.text.isEmpty() -> {
                onErrorEmail("isEmpty")
            }
        }

        when {
            !android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches() -> {
                onErrorEmail("missing Patterns Email")
            }
        }

        when {
            passwordEditText.text.isEmpty() -> {
                onErrorPassword("isEmpty")
            }
        }


        when {
            passwordEditText.text.length < 6 -> {
                onErrorPassword("More Than 6 Character")
            }
        }

        when {
            android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()
                    && passwordEditText.text.length >= 6 -> {
                onSuccess()
            }
        }
    }

    fun loginAuthentication(email: String, password: String) {
        val loadingDialogFragment: LoadingDialogFragment = LoadingDialogFragment.newInstance("ระบบกำลังล๊อคอิน...", false)
        loadingDialogFragment.onShowDialog(activity!!)

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fireBaseUser = FirebaseAuth.getInstance().currentUser!!
                uid = fireBaseUser.uid
                username = fireBaseUser.displayName.toString()
                userEmail = fireBaseUser.email.toString()
                photoUrl = fireBaseUser.photoUrl.toString()

                listEventIntent = Intent(activity, ListEventActivity::class.java)
                activity!!.startActivity(listEventIntent)

                loadingDialogFragment.dismiss()
            }
        }.addOnFailureListener { exception ->
            loadingDialogFragment.dismiss()
            Toast.makeText(activity, exception.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}

