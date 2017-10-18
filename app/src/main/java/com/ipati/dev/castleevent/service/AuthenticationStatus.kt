package com.ipati.dev.castleevent.service

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AuthenticationStatus {

    fun getCurrentUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser
}