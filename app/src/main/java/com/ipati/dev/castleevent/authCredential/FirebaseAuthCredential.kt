package com.ipati.dev.castleevent.authCredential

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.*
import com.ipati.dev.castleevent.ListEventActivity
import com.ipati.dev.castleevent.model.userManage.photoUrl
import com.ipati.dev.castleevent.model.userManage.uid
import com.ipati.dev.castleevent.model.userManage.userEmail
import com.ipati.dev.castleevent.model.userManage.username
import com.ipati.dev.castleevent.utill.SharePreferenceGoogleSignInManager
import com.twitter.sdk.android.core.TwitterSession

var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
fun facebookAuthCredential(activity: Activity, token: AccessToken) {
    val authCredential: AuthCredential = FacebookAuthProvider.getCredential(token.token)
    mAuth.signInWithCredential(authCredential).addOnCompleteListener(activity, { task ->
        if (task.isSuccessful) {
            val fireBaseUser: FirebaseUser = mAuth.currentUser!!
            updateUserProfile(activity, fireBaseUser, fireBaseUser.displayName, fireBaseUser.photoUrl, fireBaseUser.email)
            Toast.makeText(activity, fireBaseUser.email.toString(), Toast.LENGTH_SHORT).show()
        }
    })
}

fun twitterAuthCredential(activity: Activity, session: TwitterSession) {
    val authCredential: AuthCredential = TwitterAuthProvider.getCredential(session.authToken.token, session.authToken.secret)
    mAuth.signInWithCredential(authCredential).addOnCompleteListener(activity, { task ->
        if (task.isSuccessful) {
            val fireBaseUser: FirebaseUser = mAuth.currentUser!!
            updateUserProfile(activity, fireBaseUser, session.userName, fireBaseUser.photoUrl, fireBaseUser.email)
            Log.d("userTwitterLogin", session.userName.toString() + " : " + fireBaseUser.photoUrl.toString() + " : " + fireBaseUser.email.toString())
            Toast.makeText(activity, fireBaseUser.email.toString(), Toast.LENGTH_SHORT).show()
        }
    })
}

fun googleAuthCredential(activity: Activity, account: GoogleSignInAccount, mGoogleSharedPreferences: SharePreferenceGoogleSignInManager) {
    val authCredential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
    mAuth.signInWithCredential(authCredential).addOnCompleteListener(activity, { task ->
        if (task.isSuccessful) {
            val fireBaseUser: FirebaseUser = mAuth.currentUser!!
            mGoogleSharedPreferences.sharePreferenceManager(fireBaseUser.email)
            updateUserProfile(activity, fireBaseUser, fireBaseUser.displayName, fireBaseUser.photoUrl, fireBaseUser.email)
            Toast.makeText(activity, fireBaseUser.email.toString(), Toast.LENGTH_SHORT).show()
        }
    })
}

fun updateUserProfile(activity: Activity, fireBaseUser: FirebaseUser, nameUser: String?, photoUserUrl: Uri?, mUserEmail: String?) {
    val userProfileUpdate: UserProfileChangeRequest = UserProfileChangeRequest
            .Builder()
            .setDisplayName(nameUser)
            .setPhotoUri(photoUserUrl)
            .build()

    fireBaseUser.updateProfile(userProfileUpdate).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            uid = fireBaseUser.uid
            username = fireBaseUser.displayName
            photoUrl = fireBaseUser.photoUrl.toString()

            fireBaseUser.updateEmail(mUserEmail.toString()).addOnCompleteListener { fireBaseResult ->
                if (fireBaseResult.isSuccessful) {
                    userEmail = mUserEmail
                    val listEventIntent = Intent(activity, ListEventActivity::class.java)
                    activity.startActivity(listEventIntent)
                    activity.finish()
                }
            }
        }
    }
}

