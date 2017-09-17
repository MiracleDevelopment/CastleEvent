package com.ipati.dev.castleevent.model

import android.content.Context
import android.net.Uri
import android.support.v4.app.FragmentActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.ipati.dev.castleevent.ProfileUserActivity
import com.ipati.dev.castleevent.extension.onDismissDialog
import com.ipati.dev.castleevent.extension.onShowDialog
import com.ipati.dev.castleevent.fragment.loading.LoadingDialogFragment
import com.ipati.dev.castleevent.model.userManage.photoUrl
import com.ipati.dev.castleevent.model.userManage.uid
import com.ipati.dev.castleevent.model.userManage.userEmail
import com.ipati.dev.castleevent.model.userManage.username


class UserProfileUpdate(context: Context, activity: FragmentActivity) {
    private lateinit var mUserRequestChangeProfile: UserProfileChangeRequest
    private lateinit var mLoadingDialogFragment: LoadingDialogFragment
    private lateinit var mOnDismissDialogFragment: DismissDialogFragment
    private lateinit var mUploadTask: UploadTask
    private lateinit var mStatusUsername: String
    private lateinit var mStatusPassword: String
    private lateinit var mStatusEmail: String

    private var mContext: Context = context
    private var mActivity: FragmentActivity = activity
    private var fireBaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private var fireBaseStorage: StorageReference = FirebaseStorage.getInstance().reference
    private var mStatusPasswordConfirm: String = ""
    private var msgAlertDialog = "ระบบกำลังอัพเดท..."

    private fun onUpdateUsername(mUsername: String) {
        mUserRequestChangeProfile = UserProfileChangeRequest.Builder().setDisplayName(mUsername).build()
        fireBaseUser.updateProfile(mUserRequestChangeProfile).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mOnDismissDialogFragment = mContext as ProfileUserActivity
                mOnDismissDialogFragment.onChangeProfile(mUsername, 1001)

                username = mUsername
                mLoadingDialogFragment.onDismissDialog()
            }
        }.addOnFailureListener { exception ->
            mLoadingDialogFragment.onDismissDialog()
            Toast.makeText(mContext, exception.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }


    private fun onUpdatePassword(mPassword: String) {
        fireBaseUser.updatePassword(mPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mOnDismissDialogFragment = mContext as ProfileUserActivity
                mOnDismissDialogFragment.onChangeProfile(mPassword, 1002)

                Log.d("passwordUpdate", mPassword)
                mLoadingDialogFragment.onDismissDialog()

            }

        }.addOnFailureListener { exception ->
            mLoadingDialogFragment.onDismissDialog()
            Toast.makeText(mContext, exception.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun onUpdateEmail(email: String) {
        fireBaseUser.updateEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fireBaseUser.sendEmailVerification().addOnCompleteListener { taskEmail ->
                    if (taskEmail.isSuccessful) {
                        mOnDismissDialogFragment = mContext as ProfileUserActivity
                        mOnDismissDialogFragment.onChangeProfile(email, 1003)

                        userEmail = email
                        mLoadingDialogFragment.onDismissDialog()
                    }
                }
            }
        }.addOnFailureListener { exception ->
            mLoadingDialogFragment.onDismissDialog()
            Toast.makeText(mContext, exception.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    fun onUpdatePhotoUser(path: Uri) {
        val storageReference: StorageReference = fireBaseStorage.child("userProfile").child(uid.toString()).child(path.lastPathSegment)
        mUploadTask = storageReference.putFile(path).addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
            taskSnapshot?.let {
                onUpdateProfile(taskSnapshot.downloadUrl)
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(mContext, exception.message.toString(), Toast.LENGTH_SHORT).show()
        } as UploadTask
    }

    private fun onUpdateProfile(uri: Uri?) {
        mUserRequestChangeProfile = UserProfileChangeRequest.Builder().setPhotoUri(uri).build()
        fireBaseUser.updateProfile(mUserRequestChangeProfile).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mOnDismissDialogFragment = mContext as ProfileUserActivity
                mOnDismissDialogFragment.onChangeProfile(uri.toString(), 1004)

                photoUrl = uri.toString()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(mContext, exception.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    fun onValidateUsername(mUsername: String) {
        if (!TextUtils.isEmpty(username)) {
            if (mUsername != username) {
                mLoadingDialogFragment = LoadingDialogFragment.newInstance(msgAlertDialog)
                mLoadingDialogFragment.onShowDialog(activity = mActivity)
                onUpdateUsername(mUsername)
                mStatusUsername = "Success"
            } else {
                mStatusUsername = "This not Change"
            }
        } else if (TextUtils.isEmpty(mUsername)) {
            mStatusUsername = "isEmpty"
        }
    }

    fun statusUsername(): String {
        return mStatusUsername
    }

    fun onValidatePassword(password: String, mConfirmPassword: String) {
        mLoadingDialogFragment = LoadingDialogFragment.newInstance(msgAlertDialog)
        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(mConfirmPassword)) {
            if (password.length >= 6 && mConfirmPassword.length >= 6) {
                if (password == mConfirmPassword) {
                    onUpdatePassword(password)
                    mStatusPassword = "Success"

                    mLoadingDialogFragment.onShowDialog(activity = mActivity)
                }

                if (password != mConfirmPassword) {
                    mStatusPassword = "Missing Not Match"
                    mStatusPasswordConfirm = "Missing Not Match"
                }
            }
            if (password.length < 6) {
                mStatusPassword = "More 6 Character"
            }

            if (mConfirmPassword.length < 6) {
                mStatusPasswordConfirm = "More 6 Character"
            }

        } else if (!TextUtils.isEmpty(password)) {
            mStatusPassword = ""
        } else if (!TextUtils.isEmpty(mConfirmPassword)) {
            mStatusPasswordConfirm = ""
        }

        if (TextUtils.isEmpty(password)) {
            mStatusPassword = "isEmpty"
        }

        if (TextUtils.isEmpty(mConfirmPassword)) {
            mStatusPasswordConfirm = "isEmpty"
        }
    }

    fun statusPassword(): String {
        return mStatusPassword
    }

    fun statusPasswordConfirm(): String {
        return mStatusPasswordConfirm
    }

    fun onValidateEmail(email: String) {
        if (!TextUtils.isEmpty(email)) {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (email != userEmail) {
                    onUpdateEmail(email)
                    mStatusEmail = "Success"
                    mLoadingDialogFragment = LoadingDialogFragment.newInstance(msgAlertDialog)
                    mLoadingDialogFragment.onShowDialog(mActivity)
                } else {
                    mStatusEmail = "This not Change"
                }
            }
        } else if (TextUtils.isEmpty(email)) {
            mStatusEmail = "isEmpty"
        }
    }

    fun statusEmail(): String {
        return mStatusEmail
    }
}