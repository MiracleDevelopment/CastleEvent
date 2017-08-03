package com.ipati.dev.castleevent.authCredential

import android.app.Activity
import android.widget.Toast
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.*
import com.twitter.sdk.android.core.TwitterSession

var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
fun FacebookAuthCredential(activity: Activity, token: AccessToken) {
    val authCredential: AuthCredential = FacebookAuthProvider.getCredential(token.token)
    mAuth.signInWithCredential(authCredential).addOnCompleteListener(activity, { task ->
        if (task.isSuccessful) {
            Toast.makeText(activity, token.userId, Toast.LENGTH_SHORT).show()
        }
    })
}

fun TwitterAuthCredential(activity: Activity, session: TwitterSession) {
    val authCredential: AuthCredential = TwitterAuthProvider.getCredential(session.authToken.token, session.authToken.secret)
    mAuth.signInWithCredential(authCredential).addOnCompleteListener(activity, { task ->
        if (task.isSuccessful) {
            Toast.makeText(activity, session.userName, Toast.LENGTH_SHORT).show()
        }
    })
}

fun GoogleAuthCredential(activity: Activity, account: GoogleSignInAccount) {
    val authCredential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
    mAuth.signInWithCredential(authCredential).addOnCompleteListener(activity, { task ->
        if (task.isSuccessful) {
            Toast.makeText(activity, account.displayName, Toast.LENGTH_SHORT).show()
        }
    })
}