package com.ipati.dev.castleevent.authCredential

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v4.app.FragmentActivity
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.*
import com.ipati.dev.castleevent.ListEventActivity
import com.ipati.dev.castleevent.extension.onShowDialog
import com.ipati.dev.castleevent.fragment.loading.LoadingDialogFragment
import com.ipati.dev.castleevent.model.UserManager.photoUrl
import com.ipati.dev.castleevent.model.UserManager.uid
import com.ipati.dev.castleevent.model.UserManager.userEmail
import com.ipati.dev.castleevent.model.UserManager.username
import com.ipati.dev.castleevent.utill.SharePreferenceGoogleSignInManager
import com.twitter.sdk.android.core.TwitterSession

var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
fun facebookAuthCredential(activity: FragmentActivity, token: AccessToken) {
    val authCredential: AuthCredential = FacebookAuthProvider.getCredential(token.token)
    val loadingDialogFragment: LoadingDialogFragment = LoadingDialogFragment.newInstance("กำลังล๊อคอินผ่าน Facebook...", false)
    loadingDialogFragment.onShowDialog(activity)

    mAuth.signInWithCredential(authCredential).addOnCompleteListener(activity, { task ->
        if (task.isSuccessful) {
            val fireBaseUser: FirebaseUser = mAuth.currentUser!!
            updateUserProfile(activity, fireBaseUser, fireBaseUser.displayName, fireBaseUser.photoUrl, fireBaseUser.email, loadingDialogFragment)
        } else {
            loadingDialogFragment.dismiss()
        }
    })
}

fun twitterAuthCredential(activity: FragmentActivity, session: TwitterSession) {
    val authCredential: AuthCredential = TwitterAuthProvider.getCredential(session.authToken.token, session.authToken.secret)
    val loadingDialogFragment: LoadingDialogFragment = LoadingDialogFragment.newInstance("กำลังล๊ลอคอินผ่าน Twitter...", false)
    loadingDialogFragment.onShowDialog(activity)

    mAuth.signInWithCredential(authCredential).addOnCompleteListener(activity, { task ->
        if (task.isSuccessful) {
            val fireBaseUser: FirebaseUser = mAuth.currentUser!!
            updateUserProfile(activity, fireBaseUser, session.userName, fireBaseUser.photoUrl, fireBaseUser.email, loadingDialogFragment)

        } else {
            loadingDialogFragment.dismiss()
        }
    })
}

fun googleAuthCredential(activity: FragmentActivity, account: GoogleSignInAccount, mGoogleSharedPreferences: SharePreferenceGoogleSignInManager) {
    val authCredential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
    val loadingDialogFragment: LoadingDialogFragment = LoadingDialogFragment.newInstance("กำลังล๊อคอินผ่าน Google...", false)
    loadingDialogFragment.onShowDialog(activity)

    mAuth.signInWithCredential(authCredential).addOnCompleteListener(activity, { task ->
        if (task.isSuccessful) {
            val fireBaseUser: FirebaseUser = mAuth.currentUser!!
            mGoogleSharedPreferences.sharePreferenceManager(fireBaseUser.email)
            updateUserProfile(activity, fireBaseUser, fireBaseUser.displayName, fireBaseUser.photoUrl, fireBaseUser.email, loadingDialogFragment)
        } else {
            loadingDialogFragment.dismiss()
        }
    })
}

fun updateUserProfile(activity: Activity, fireBaseUser: FirebaseUser, nameUser: String?, photoUserUrl: Uri?, mUserEmail: String?, loadingDialogFragment: LoadingDialogFragment) {
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
                    loadingDialogFragment.dismiss()
                    activity.finish()
                } else {
                    loadingDialogFragment.dismiss()
                }
            }
        } else {
            loadingDialogFragment.dismiss()
        }
    }
}

