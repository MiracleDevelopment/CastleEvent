package com.ipati.dev.castleevent.model

import android.content.Context
import android.net.Uri
import android.support.v4.app.FragmentActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.ipati.dev.castleevent.LibPerson.FireBaseWrapper
import com.ipati.dev.castleevent.ProfileUserActivity
import com.ipati.dev.castleevent.extension.onShowMissingDialog
import com.ipati.dev.castleevent.extension.onShowToast
import com.ipati.dev.castleevent.model.UserManager.*
import kotlin.collections.HashMap

class UserProfileUpdate(context: Context) {
    private lateinit var userRequestChangeProfile: UserProfileChangeRequest
    private lateinit var onDismissDialogFragment: DismissDialogFragment
    private lateinit var onChangeProgressUserPhoto: OnProgressPhotoUser
    private lateinit var uploadTask: UploadTask

    private var contextProfileManager: Context = context
    private var fireBaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private var fireBaseStorage: StorageReference = FirebaseStorage.getInstance().reference

    private lateinit var fireBaseWrapper: FireBaseWrapper
    fun onChangeProfileUser(userAccount: String, password: String, userEmail: String
                            , callBackCompleteChangeProfile: (() -> Unit), callBackOnFailure: ((e: Exception) -> Unit)) {
        userRequestChangeProfile = UserProfileChangeRequest.Builder()
                .setDisplayName(userAccount)
                .build()

        val fireBaseListTask: List<Task<Void>> = listOf(fireBaseUser.updateProfile(userRequestChangeProfile)
                , fireBaseUser.updatePassword(password), fireBaseUser.updateEmail(userEmail))

        fireBaseWrapper = FireBaseWrapper(fireBaseListTask).apply {
            addCompleteSuccess = {
                callBackCompleteChangeProfile()
            }
            addOnFailure = { errorMessage ->
                callBackOnFailure(errorMessage)
            }
        }
    }

    fun onCheckStateChange(password: String, rePassword: String, emailUser: String
                           , errorPassword: ((String) -> Unit)
                           , errorEmail: ((String) -> Unit)): Boolean {

        when (password) {
            rePassword -> {
                if (password.length < 6 || rePassword.length < 6) {
                    errorPassword("More than 6 Character")
                }
            }
            else -> {
                errorPassword("not Equal Password")
            }

        }

        when {
            !android.util.Patterns.EMAIL_ADDRESS.matcher(emailUser).matches() -> {
                errorEmail("missing Patterns Email ")
            }
        }

        when {
            password == rePassword && password.length >= 6 && rePassword.length >= 6
                    && android.util.Patterns.EMAIL_ADDRESS.matcher(emailUser).matches() -> {
                return true
            }
        }
        return false
    }

    fun onUpdatePhotoUser(path: Uri, onSuccess: (() -> Unit), onFailure: ((exception: Exception) -> Unit)) {
        val storageReference: StorageReference = fireBaseStorage.child("userProfile")
                .child(uid.toString())
                .child(path.lastPathSegment)

        uploadTask = storageReference.putFile(path).addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
            taskSnapshot?.let {
                onUpdateProfile(taskSnapshot.downloadUrl, {
                    onSuccess()
                }, { exception: Exception ->
                    onFailure(exception)
                })
            }
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }.addOnProgressListener { taskSnapshot ->
            onChangeProgressUserPhoto = contextProfileManager as ProfileUserActivity
            onChangeProgressUserPhoto.setProgressUserPhoto(((100 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount).toInt())

        } as UploadTask
    }

    private fun onUpdateProfile(uri: Uri?, onSuccessChangeProfile: (() -> Unit), onFailure: (exception: Exception) -> Unit) {
        userRequestChangeProfile = UserProfileChangeRequest.Builder().setPhotoUri(uri).build()
        fireBaseUser.updateProfile(userRequestChangeProfile).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onDismissDialogFragment = contextProfileManager as ProfileUserActivity
                onDismissDialogFragment.onChangeProfile(uri.toString(), requestChangeProfile)

                photoUrl = uri.toString()
                onSuccessChangeProfile()

            }
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }

    fun onChangeUserProfileDate(updateChild: String, birthDay: Long, gender: Int) {
        val refRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
        val refUserProfile: DatabaseReference = refRoot.child("userProfile").child(uid!!).child(updateChild)

        val mapData: HashMap<String, Any> = HashMap()
        mapData.put("dateUser", birthDay)
        mapData.put("gender", gender)

        refUserProfile.updateChildren(mapData).addOnCompleteListener { task ->
            when {
                task.isSuccessful -> {
                    contextProfileManager.onShowToast("AfterChangeProfile")
                }
                else -> {
                    contextProfileManager.onShowToast(task.exception?.message.toString())
                }
            }
        }
    }

    fun onChangeUserProfileDateFirst(birthDay: Long, gender: Int, phoneNumber: String) {
        val refRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
        val refUserProfile: DatabaseReference = refRoot.child("userProfile").child(uid)
        val extendProfile = ExtendedProfileUserModel(gender, birthDay, phoneNumber)

        refUserProfile.push().setValue(extendProfile).addOnCompleteListener {
            when {
                it.isSuccessful -> {
                    contextProfileManager.onShowToast("FirstChangeProfile")
                }
                else -> {
                    contextProfileManager.onShowToast(it.exception?.message.toString())
                }
            }
        }
    }

    fun reAuthentication(activity: FragmentActivity, callBackReAuthenticationSuccess: (() -> Unit)) {
        onShowMissingDialog(activity, "คุณเข้าสู่ระบบนานเกินไป กรุณาล๊อคอินใหม่อีกครั้ง", requestReAuthentication).apply {
            callBackReAuthentication = {
                callBackReAuthenticationSuccess()
            }
        }
    }

    companion object {
        private const val requestChangeProfile = 1111
        private const val requestReAuthentication = 1112
    }

}