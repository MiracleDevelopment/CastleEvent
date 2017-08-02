package com.ipati.dev.castleevent.authCredential

import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.twitter.sdk.android.core.TwitterSession

var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

fun FacebookAuthCredential(token: AccessToken) {
    val authCredential: AuthCredential = FacebookAuthProvider.getCredential(token.token)
    mAuth.signInWithCredential(authCredential).addOnCompleteListener { task: Task<AuthResult> ->
        if (task.isSuccessful) {
            val user: FirebaseUser = mAuth.currentUser!!
            Log.d("FacebookLogin", user.displayName)
        }
    }.addOnFailureListener { exception ->
        Log.d("FacebookLoginException", exception.message.toString())

    }
}

fun TwitterAuthCredential(session: TwitterSession) {

}

fun GoogleAuthCredential(account: GoogleSignInAccount) {

}