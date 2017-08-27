package com.ipati.dev.castleevent.authCredential

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.Profile
import com.facebook.ProfileTracker
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.*
import com.ipati.dev.castleevent.LoginActivity
import com.ipati.dev.castleevent.fragment.LoginFragment
import com.ipati.dev.castleevent.model.LoadingListener
import com.ipati.dev.castleevent.model.ShowListEventFragment
import com.ipati.dev.castleevent.model.userManage.email
import com.ipati.dev.castleevent.model.userManage.photoUrl
import com.ipati.dev.castleevent.model.userManage.username
import com.ipati.dev.castleevent.service.loadingListener
import com.twitter.sdk.android.core.TwitterSession

var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
var listEventListener: ShowListEventFragment? = null
fun facebookAuthCredential(loadingListener: LoadingListener?, activity: Activity, token: AccessToken) {
    val authCredential: AuthCredential = FacebookAuthProvider.getCredential(token.token)
    listEventListener = activity as LoginActivity

    mAuth.signInWithCredential(authCredential).addOnCompleteListener(activity, { task ->
        if (task.isSuccessful) {
            val fireBaseUser: FirebaseUser = mAuth.currentUser!!
            updateUserProfile(fireBaseUser, fireBaseUser.displayName, fireBaseUser.photoUrl, fireBaseUser.email)
            Toast.makeText(activity, fireBaseUser.email.toString(), Toast.LENGTH_SHORT).show()
        } else {
            loadingListener?.onHindLoading(false)
        }
    })
}

fun twitterAuthCredential(loadingListener: LoadingListener?, activity: Activity, session: TwitterSession) {
    val authCredential: AuthCredential = TwitterAuthProvider.getCredential(session.authToken.token, session.authToken.secret)
    listEventListener = activity as LoginActivity
    loadingListener?.onShowLoading(true)
    mAuth.signInWithCredential(authCredential).addOnCompleteListener(activity, { task ->
        if (task.isSuccessful) {
            val fireBaseUser: FirebaseUser = mAuth.currentUser!!
            updateUserProfile(fireBaseUser, session.userName, fireBaseUser.photoUrl, fireBaseUser.email)
            Log.d("userTwitterLogin", session.userName.toString() + " : " + fireBaseUser.photoUrl.toString() + " : " + fireBaseUser.email.toString())
            Toast.makeText(activity, fireBaseUser.email.toString(), Toast.LENGTH_SHORT).show()
        } else {
            loadingListener?.onHindLoading(false)
        }
    })
}

fun googleAuthCredential(activity: Activity, account: GoogleSignInAccount) {
    val authCredential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
    loadingListener = activity as LoginActivity
    loadingListener?.onShowLoading(true)

    mAuth.signInWithCredential(authCredential).addOnCompleteListener(activity, { task ->
        if (task.isSuccessful) {
            val fireBaseUser: FirebaseUser = mAuth.currentUser!!
            updateUserProfile(fireBaseUser, fireBaseUser.displayName, fireBaseUser.photoUrl, fireBaseUser.email)
            Toast.makeText(activity, fireBaseUser.email.toString(), Toast.LENGTH_SHORT).show()
        }
    })
}

fun updateUserProfile(fireBaseUser: FirebaseUser, nameUser: String?, photoUserUrl: Uri?, userEmail: String?) {
    val userProfileUpdate: UserProfileChangeRequest = UserProfileChangeRequest
            .Builder()
            .setDisplayName(nameUser)
            .setPhotoUri(photoUserUrl)
            .build()

    fireBaseUser.updateProfile(userProfileUpdate).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            username = fireBaseUser.displayName
            photoUrl = fireBaseUser.photoUrl.toString()

            fireBaseUser.updateEmail(userEmail.toString()).addOnCompleteListener { fireBaseResult ->
                if (fireBaseResult.isSuccessful) {
                    loadingListener?.onHindLoading(false)
                    listEventListener?.onShowListFragment()
                    email = fireBaseUser.email
                }
            }
        }
    }
}
