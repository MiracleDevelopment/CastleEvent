package com.ipati.dev.castleevent.authCredential

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.*
import com.ipati.dev.castleevent.LoginActivity
import com.ipati.dev.castleevent.model.LoadingListener
import com.ipati.dev.castleevent.model.ShowListEventFragment
import com.ipati.dev.castleevent.service.loadingListener
import com.twitter.sdk.android.core.TwitterSession

var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
var listEventListener: ShowListEventFragment? = null
fun FacebookAuthCredential(loadingListener: LoadingListener?, activity: Activity, token: AccessToken) {
    val authCredential: AuthCredential = FacebookAuthProvider.getCredential(token.token)
    listEventListener = activity as LoginActivity

    mAuth.signInWithCredential(authCredential).addOnCompleteListener(activity, { task ->
        if (task.isSuccessful) {
            val fireBaseUser: FirebaseUser = mAuth.currentUser!!
            UpdateUserProfile(fireBaseUser, fireBaseUser.displayName, fireBaseUser.photoUrl)
        } else {
            loadingListener?.onHindLoading(false)
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

fun UpdateUserProfile(fireBaseUser: FirebaseUser, nameUser: String?, photoUserUrl: Uri?) {
    val userProfileUpdate: UserProfileChangeRequest = UserProfileChangeRequest
            .Builder()
            .setDisplayName(nameUser)
            .setPhotoUri(photoUserUrl)
            .build()

    fireBaseUser.updateProfile(userProfileUpdate).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            loadingListener?.onHindLoading(false)
            listEventListener?.onShowListFragment()
        }
    }
}
