package com.ipati.dev.castleevent.fragment


import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieDrawable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.extension.matrixHeightPx
import com.ipati.dev.castleevent.model.register.RegisterManager
import kotlinx.android.synthetic.main.activity_register_fragment.*
import java.lang.Exception

class RegisterFragment : Fragment() {
    private lateinit var registerManager: RegisterManager
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fireBaseUser: FirebaseUser
    private lateinit var fireBaseUpdateProfile: UserProfileChangeRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mAuth = FirebaseAuth.getInstance()
        registerManager = RegisterManager(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_register_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialToolbar()
        initialLottieAnimation()

        tv_success_register.setOnClickListener {
            initialValidateAccount()
        }
    }

    private fun initialLottieAnimation() {
        lottie_view_animation_register.layoutParams.height = context.matrixHeightPx(450)

        val lottieDrawable = LottieDrawable()
        LottieComposition.Factory.fromAssetFileName(context, "login_animation.json", { composition ->
            lottieDrawable.composition = composition
            lottie_view_animation_register.setImageDrawable(lottieDrawable)
        })

    }

    private fun initialToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar_register)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            title = ""
        }
    }

    private fun initialValidateAccount() {
        when {
            TextUtils.isEmpty(register_ed_email.text.toString()) -> {
                register_ed_email.error = "Please put your Email"
            }
        }

        when {
            TextUtils.isEmpty(register_ed_username.text.toString()) -> {
                register_ed_username.error = "Please put your username"
            }
        }

        when {
            TextUtils.isEmpty(register_ed_password.text.toString()) -> {
                register_ed_password.error = "Please put your password"
            }
        }

        when {
            TextUtils.isEmpty(register_ed_re_password.text.toString()) -> {
                register_ed_re_password.error = "Please put your re-password"
            }
        }

        when {
            TextUtils.isEmpty(register_ed_phone.text.toString()) -> {
                register_ed_phone.error = "Please put your number phone"
            }
        }

        when {
            !TextUtils.isEmpty(register_ed_email.text.toString()) &&
                    !TextUtils.isEmpty(register_ed_username.text.toString()) &&
                    !TextUtils.isEmpty(register_ed_password.text.toString()) &&
                    !TextUtils.isEmpty(register_ed_re_password.text.toString()) &&
                    !TextUtils.isEmpty(register_ed_phone.text.toString()) -> {
                when (true) {
                    android.util.Patterns.EMAIL_ADDRESS.matcher(register_ed_email.text.toString()).matches() -> {
                        when (true) {
                            android.util.Patterns.PHONE.matcher(register_ed_phone.text.toString()).matches() -> {
                                when (register_ed_password.text.toString()) {
                                    register_ed_re_password.text.toString() -> {
                                        when (register_ed_password.length()) {
                                            in 6..10 -> {
                                                stateRegisterListener(register_ed_email.text.toString()
                                                        , register_ed_password.text.toString())
                                            }
                                            else -> {
                                                register_ed_password.error = "Password Less 6 Character"
                                            }
                                        }
                                    }
                                    else -> {
                                        register_ed_re_password.error = "Missing Confirm Password"
                                    }
                                }
                            }
                            else -> {
                                register_ed_phone.error = "Please put - between number phone"
                            }
                        }
                    }
                    else -> {
                        register_ed_email.error = "missing pattern userEmail"
                    }
                }
            }
        }
    }

    private fun stateRegisterListener(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { resultLogin ->
            if (resultLogin.isSuccessful) {
                //todo: Think miss position this
                fireBaseUser = FirebaseAuth.getInstance().currentUser!!
                fireBaseUpdateProfile = UserProfileChangeRequest.Builder()
                        .setDisplayName(register_ed_username.text.toString())
                        .setPhotoUri(Uri.parse("https://static.pexels.com/photos/126407/pexels-photo-126407.jpeg"))
                        .build()
                fireBaseUser.updatePassword(register_ed_password.text.toString()).addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        fireBaseUser.updateProfile(fireBaseUpdateProfile).addOnCompleteListener { resultPassword ->
                            if (resultPassword.isSuccessful) {
                                activity.finish()
                            } else {
                                Toast.makeText(context, "fail Update Profile User", Toast.LENGTH_SHORT).show()
                            }
                        }.addOnFailureListener { exception ->
                            Toast.makeText(context, exception.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(context, exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
            } else {
                register_ed_email.error = "The userEmail address is already"
                Toast.makeText(context, "The userEmail address is already in use by another account", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception: Exception ->
            Toast.makeText(context, exception.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity.supportFinishAfterTransition()
            }
        }
        return super.onOptionsItemSelected(item)
    }



    companion object {
        fun newInstance(): RegisterFragment {
            return RegisterFragment().apply { arguments = Bundle() }
        }
    }
}
