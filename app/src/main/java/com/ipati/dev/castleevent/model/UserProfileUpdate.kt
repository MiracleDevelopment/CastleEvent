package com.ipati.dev.castleevent.model

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.ipati.dev.castleevent.model.userManage.userEmail
import com.ipati.dev.castleevent.model.userManage.username


class UserProfileUpdate(context: Context) {
    private lateinit var mUserRequestChangeProfile: UserProfileChangeRequest
    private lateinit var mStatusUsername: String
    private lateinit var mStatusPassword: String
    private lateinit var mStatusEmail: String

    private var mContext: Context = context
    private var fireBaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private var mStatusPasswordConfirm: String = ""

    private fun onUpdateUsername(mUsername: String) {
        if (mUsername != username) {
            mUserRequestChangeProfile = UserProfileChangeRequest.Builder().setDisplayName(mUsername).build()
            fireBaseUser.updateProfile(mUserRequestChangeProfile).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    username = mUsername
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(mContext, exception.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun onUpdatePassword(mPassword: String) {
        fireBaseUser.updatePassword(mPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("passwordUpdate", mPassword)
            }

        }.addOnFailureListener { exception ->
            Toast.makeText(mContext, exception.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun onUpdateEmail(email: String) {
        if (email != userEmail) {
            fireBaseUser.updateEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    fireBaseUser.sendEmailVerification().addOnCompleteListener { taskEmail ->
                        if (taskEmail.isSuccessful) {
                            userEmail = email
                        }
                    }
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(mContext, exception.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onValidateUsername(username: String) {
        if (!TextUtils.isEmpty(username)) {
            onUpdateUsername(username)
            mStatusUsername = "Success"
        } else if (TextUtils.isEmpty(username)) {
            mStatusUsername = "isEmpty"
        }
    }

    fun statusUsername(): String {
        return mStatusUsername
    }

    fun onValidatePassword(password: String, mConfirmPassword: String) {
        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(mConfirmPassword)) {
            if (password.length >= 6 && mConfirmPassword.length >= 6) {
                if (password == mConfirmPassword) {
                    onUpdatePassword(password)
                    mStatusPassword = "Success"
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
                onUpdateEmail(email)
                mStatusEmail = "Success"
            }
        } else if (TextUtils.isEmpty(email)) {
            mStatusEmail = "isEmpty"
        }
    }

    fun statusEmail(): String {
        return mStatusEmail
    }
}