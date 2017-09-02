package com.ipati.dev.castleevent.fragment


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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.register.RegisterManager
import kotlinx.android.synthetic.main.activity_register_fragment.*
import java.lang.Exception

class RegisterFragment : Fragment() {
    lateinit var registerManager: RegisterManager
    lateinit var mAuth: FirebaseAuth
    lateinit var fireBaseUser: FirebaseUser
    lateinit var fireBaseUpdateProfile: UserProfileChangeRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        registerManager = RegisterManager(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_register_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        initialToolbar()

        tv_success_register.setOnClickListener {
            initialValidateAccount()
        }
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
                                        stateRegisterListener(register_ed_email.text.toString()
                                                , register_ed_password.text.toString())
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
                        register_ed_email.error = "missing pattern email"
                    }
                }
            }
        }
    }

    private fun stateRegisterListener(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { resultLogin ->
            if (resultLogin.isSuccessful) {
                fireBaseUser = FirebaseAuth.getInstance().currentUser!!
                fireBaseUpdateProfile = UserProfileChangeRequest.Builder()
                        .setDisplayName(register_ed_username.text.toString())
                        .setPhotoUri(Uri.parse("https://static.pexels.com/photos/126407/pexels-photo-126407.jpeg"))
                        .build()

                fireBaseUser.updateProfile(fireBaseUpdateProfile).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Register Success", Toast.LENGTH_SHORT).show()
                        activity.finish()
                    } else {
                        Toast.makeText(context, "fail Update Profile User", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(context, exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "The email address is already in use by another account", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception: Exception ->
            Toast.makeText(context, exception.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()
    }

    companion object {
        fun newInstance(): RegisterFragment {
            return RegisterFragment().apply { arguments = Bundle() }
        }
    }
}
