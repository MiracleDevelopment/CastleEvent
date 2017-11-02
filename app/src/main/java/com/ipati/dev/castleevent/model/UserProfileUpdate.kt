package com.ipati.dev.castleevent.model

import android.content.Context
import android.net.Uri
import android.support.design.widget.TextInputLayout
import android.support.v4.app.FragmentActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.androidhuman.rxfirebase2.auth.*
import com.facebook.internal.Utility
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.ipati.dev.castleevent.ProfileUserActivity
import com.ipati.dev.castleevent.extension.onShowDialog
import com.ipati.dev.castleevent.extension.onShowLoadingDialog
import com.ipati.dev.castleevent.fragment.loading.LoadingDialogFragment
import com.ipati.dev.castleevent.model.UserManager.photoUrl
import com.ipati.dev.castleevent.model.UserManager.uid
import com.ipati.dev.castleevent.model.UserManager.userEmail
import com.ipati.dev.castleevent.model.UserManager.username

class UserProfileUpdate(context: Context, inputUsername: TextInputLayout, inputPassword: TextInputLayout
                        , inputRePassword: TextInputLayout, inputEmail: TextInputLayout, activity: FragmentActivity) {
    private lateinit var userRequestChangeProfile: UserProfileChangeRequest
    private lateinit var loadingDialogFragment: LoadingDialogFragment
    private lateinit var onDismissDialogFragment: DismissDialogFragment
    private lateinit var onChangeProgressUserPhoto: OnProgressPhotoUser
    private lateinit var uploadTask: UploadTask

    private var contextProfileManager: Context = context
    private var activityProfileManager: FragmentActivity = activity
    private var fireBaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private var fireBaseStorage: StorageReference = FirebaseStorage.getInstance().reference

    private var usernameLayout: TextInputLayout = inputUsername
    private var passwordLayout: TextInputLayout = inputPassword
    private var rePasswordLayout: TextInputLayout = inputRePassword
    private var emailLayout: TextInputLayout = inputEmail

    var callBackUserProfileChange: ((String) -> Unit)? = null
    var callBackPassword: ((String) -> Unit)? = null
    var callBackEmail: ((String) -> Unit)? = null

    fun onChangeProfileUser(userAccount: String, password: String, rePassword: String, userEmail: String) {

        val loadingDialog = onShowLoadingDialog(activityProfileManager, "คุณกำลังอัพเดทโปรไฟล์...")
        userRequestChangeProfile = UserProfileChangeRequest.Builder()
                .setDisplayName(userAccount)
                .build()
        onDismissDialogFragment = activityProfileManager as ProfileUserActivity

        fireBaseUser.rxUpdateProfile(userRequestChangeProfile).subscribe({
            //Todo: success
            onDismissDialogFragment.onChangeProfile(userAccount, 1008)

            fireBaseUser.rxUpdatePassword(password).subscribe({
                //Todo: success
                onDismissDialogFragment.onChangeProfile(password, 1009)

                fireBaseUser.rxUpdateEmail(userEmail).subscribe({
                    //Todo: success
                    onDismissDialogFragment.onChangeProfile(userEmail, 1010)
                    loadingDialog.dismiss()

                    //Todo: EndProcessEmail
                }) { t: Throwable? ->
                    loadingDialog.dismiss()
                    Log.d("onErrorEmail", t?.message.toString())
                }

                //Todo: EndProcessPassword
            }) { t: Throwable? ->
                loadingDialog.dismiss()
                Log.d("onErrorPassword", t?.message.toString())
            }
        })
    }


    fun onCheckStateChange(userAccount: String, password: String, rePassword: String, emailUser: String): Boolean {
        if (userAccount.isEmpty()) {
            callBackUserProfileChange?.invoke("specific username")
            return false
        } else if (userAccount == username) {
            callBackUserProfileChange?.invoke("already username")
            return false
        } else {
            usernameLayout.isErrorEnabled = false
        }

        if (password.isEmpty() || rePassword.isEmpty()) {
            callBackPassword?.invoke("specific password")
            return false
        } else if (password != rePassword || password.length != rePassword.length) {
            callBackPassword?.invoke("not Match")
            return false
        } else {
            passwordLayout.isErrorEnabled = false
            rePasswordLayout.isErrorEnabled = false
        }

        if (emailUser.isEmpty()) {
            callBackEmail?.invoke("specific email")
            return false
        } else if (emailUser != userEmail) {
            callBackEmail?.invoke("already email")
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()) {
            callBackEmail?.invoke("not Match Pattern Email")
            return false
        } else {
            emailLayout.isErrorEnabled = false
        }

        if (!userAccount.isEmpty()
                && !password.isEmpty()
                && !rePassword.isEmpty()
                && !emailUser.isEmpty()) {

            return true
        }

        return false
    }


    fun onUpdatePhotoUser(path: Uri) {
        loadingDialogFragment = LoadingDialogFragment.newInstance("กำลังอัพโหลดรูปโปรไฟล์...", true)
        loadingDialogFragment.onShowDialog(activityProfileManager)

        val storageReference: StorageReference = fireBaseStorage.child("userProfile").child(uid.toString()).child(path.lastPathSegment)
        uploadTask = storageReference.putFile(path).addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
            taskSnapshot?.let {
                onUpdateProfile(taskSnapshot.downloadUrl)
            }

        }.addOnFailureListener { exception ->
            loadingDialogFragment.dismiss()
            Toast.makeText(contextProfileManager, exception.message.toString(), Toast.LENGTH_SHORT).show()

        }.addOnProgressListener { taskSnapshot ->
            onChangeProgressUserPhoto = contextProfileManager as ProfileUserActivity
            onChangeProgressUserPhoto.setProgressUserPhoto(((100 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount).toInt())

        } as UploadTask
    }

    private fun onUpdateProfile(uri: Uri?) {
        userRequestChangeProfile = UserProfileChangeRequest.Builder().setPhotoUri(uri).build()
        fireBaseUser.updateProfile(userRequestChangeProfile).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onDismissDialogFragment = contextProfileManager as ProfileUserActivity
                onDismissDialogFragment.onChangeProfile(uri.toString(), 1111)

                loadingDialogFragment.dismiss()
                photoUrl = uri.toString()
            }
        }.addOnFailureListener { exception ->
            loadingDialogFragment.dismiss()
            Toast.makeText(contextProfileManager, exception.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

}