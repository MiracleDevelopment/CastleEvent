package com.ipati.dev.castleevent.fragment


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.Toast
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieDrawable
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.extension.*
import com.ipati.dev.castleevent.fragment.loading.LoadingDialogFragment
import com.ipati.dev.castleevent.model.Register.RegisterManager
import com.ipati.dev.castleevent.model.UserManager.ExtendedProfileUserModel
import com.ipati.dev.castleevent.model.UserManager.birthDay
import com.ipati.dev.castleevent.model.UserManager.phoneNumber
import com.ipati.dev.castleevent.model.UserManager.uidRegister
import kotlinx.android.synthetic.main.activity_register_fragment.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class RegisterFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var registerManager: RegisterManager
    private lateinit var auth: FirebaseAuth
    private lateinit var fireBaseUser: FirebaseUser
    private lateinit var fireBaseUpdateProfile: UserProfileChangeRequest
    private lateinit var loadingDialogFragment: LoadingDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        auth = FirebaseAuth.getInstance()
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


        register_birth_day.setOnClickListener {
            DatePickerDialog(context, this, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH).show()
        }

    }

    private fun initialLottieAnimation() {
        lottie_view_animation_register.layoutParams.height = context.pxToDp(280)

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
                                                loadingDialogFragment = onShowLoadingDialog(activity, "ระบบกำลังสมัครสมาชิก...", false)
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
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { resultLogin ->
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
                                val birthDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("th"))
                                val dateTime: Date? = birthDateFormat.parse(register_birth_day.text.toString())
                                Log.d("dateTime", dateTime.toString())
                                birthDay = dateTime?.time
                                phoneNumber = register_ed_phone.text.toString()

                                loadingDialogFragment.onDismissDialog()
                                uidRegister = resultLogin.result.user.uid
                                onShowRegisterDialogFragment(activity.supportFragmentManager)
                            } else {
                                loadingDialogFragment.onDismissDialog()
                                Toast.makeText(context, "fail Update Profile User", Toast.LENGTH_SHORT).show()
                            }
                        }.addOnFailureListener { exception ->
                            loadingDialogFragment.onDismissDialog()
                            Toast.makeText(context, exception.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener { exception ->
                    loadingDialogFragment.onDismissDialog()
                    Toast.makeText(context, exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
            } else {
                loadingDialogFragment.onDismissDialog()
                register_ed_email.error = "The userEmail address is already"
                Toast.makeText(context, "The userEmail address is already in use by another account", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception: Exception ->
            loadingDialogFragment.onDismissDialog()
            Toast.makeText(context, exception.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        register_birth_day.setText("$p3/$p2/$p1")
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
        fun newInstance(): RegisterFragment = RegisterFragment().apply {
            arguments = Bundle()
        }
    }
}
