package com.ipati.dev.castleevent.service

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginAuthManager(context: Context) {
    private var mContext: Context = context
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    fun loginAuthentication(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(mContext, "CredentialSuccess", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(mContext, exception.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    fun stateLoginListener() {
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val fireBaseUser = firebaseAuth.currentUser
            if (fireBaseUser != null) {

            }
        }
    }
}

