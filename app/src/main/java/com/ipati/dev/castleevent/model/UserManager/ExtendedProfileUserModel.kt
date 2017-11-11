package com.ipati.dev.castleevent.model.UserManager

import android.support.annotation.Keep


@Keep
data class ExtendedProfileUserModel(var gender: Int? = 0, var dateUser: Long? = 0, var phone: String = "")

