package com.ipati.dev.castleevent.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.Fresco.loadPhotoUserProfile
import com.ipati.dev.castleevent.model.UserManager.*
import com.ipati.dev.castleevent.model.UserProfileUpdate
import com.ipati.dev.castleevent.service.FirebaseService.ProfileUserFragmentManager
import kotlinx.android.synthetic.main.activity_profile_user_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProfileUserFragment : Fragment(), View.OnClickListener {
    private lateinit var editTableChangeText: EditableChangeText
    private lateinit var listItemEditText: ArrayList<DataEditText>
    private lateinit var userProfileManager: ProfileUserFragmentManager
    private lateinit var calendarBirthDay: Calendar
    private val changeProfileUser: UserProfileUpdate by lazy {
        UserProfileUpdate(context, tv_input_username_profile
                , tv_input_password_profile
                , tv_input_re_password
                , tv_input_email_profile
                , activity)
    }

    private var stateClickable: Boolean = false
    private var updateChild: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_profile_user_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userProfileManager = ProfileUserFragmentManager(lifecycle)
        initialToolbar()
        initialEditText()
        getLanguageDefault()

        changeProfileUser.callBackUserProfileChange = {
            tv_input_username_profile.error = it
        }

        changeProfileUser.callBackPassword = {
            tv_input_password_profile.error = it
            tv_input_re_password.error = it
        }

        changeProfileUser.callBackEmail = {
            tv_input_email_profile.error = it
        }

        li_gender_male.setOnClickListener(this)
        li_gender_female.setOnClickListener(this)

    }

    private fun getLanguageDefault() {
        if (Locale.getDefault().language == "en") {
            im_edit_photo_profile.hierarchy.setOverlayImage(ContextCompat.getDrawable(context, R.mipmap.crop_image_eng))
        } else {
            im_edit_photo_profile.hierarchy.setOverlayImage(ContextCompat.getDrawable(context, R.mipmap.crop_image))
        }
    }

    private fun initialToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar_user_profile)
            supportActionBar?.apply {
                title = ""
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }
    }

    private fun initialEditText() {
        loadPhotoUserProfile(context, photoUrl.toString(), im_edit_photo_profile)

        ed_account_name_profile.setText(username)
        ed_email_profile.setText(userEmail)

        tv_record_profile.setOnClickListener { view -> onClick(view) }
        im_edit_photo_profile.setOnClickListener { view -> onClick(view) }
        im_edit_photo_profile.setOnLongClickListener { tv_show_upload.visibility = View.VISIBLE; return@setOnLongClickListener false }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_record_profile -> {
                if (changeProfileUser.onCheckStateChange(ed_account_name_profile.text.toString()
                        , ed_account_pass_profile.text.toString()
                        , ed_re_account_password_profile.text.toString()
                        , ed_email_profile.text.toString())) {

                    changeProfileUser.onChangeProfileUser(ed_account_name_profile.text.toString()
                            , ed_account_pass_profile.text.toString()
                            , ed_re_account_password_profile.text.toString()
                            , ed_email_profile.text.toString())

                    updateChild?.let {
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("th"))
                        val timeMillion: Date? = dateFormat.parse(ed_birth_day.text.toString())
                        changeProfileUser.onChangeUserProfileDate(updateChild!!, timeMillion?.time!!
                                , gender = if (li_gender_female.isActivated) 0 else 1)
                    }
                }
            }

            R.id.im_edit_photo_profile -> {
                tv_show_upload.visibility = View.VISIBLE
                val intentPhoto = Intent(Intent.ACTION_GET_CONTENT)
                intentPhoto.type = "image/*"
                startActivityForResult(Intent.createChooser(intentPhoto, "Choose Image Profile"), REQUEST_PHOTO)
            }

            R.id.li_gender_male -> {
                enableMale()
                tv_record_profile.setBackgroundResource(R.drawable.custom_back_ground_accept)
                stateClickable = true
            }

            R.id.li_gender_female -> {
                disableFemale()
                tv_record_profile.setBackgroundResource(R.drawable.custom_back_ground_accept)
                stateClickable = true
            }
        }
    }

    private fun enableMale() {
        li_gender_male.isActivated = true
        li_gender_female.isActivated = false
    }

    private fun disableFemale() {
        li_gender_female.isActivated = true
        li_gender_male.isActivated = false
    }

    //Todo: Change Profile Fragment from activity
    fun onChangeUserPhoto(msg: String) {
        photoUrl = msg
        loadPhotoUserProfile(context, msg, im_edit_photo_profile)
    }

    //Todo: ChangeUsername from activity
    fun onChangeUsername(userAccount: String) {
        username = userAccount
        ed_account_name_profile.setText(userAccount)
    }

    //Todo : ChangePassword from activity
    fun onChangePassword(password: String) {
        ed_account_pass_profile.setText(password)
        ed_re_account_password_profile.setText(password)
    }

    //Todo: ChangeEmail from activity
    fun onChangeEmail(email: String) {
        userEmail = email
        ed_email_profile.setText(email)
    }

    private fun addItemEditText() {
        listItemEditText = ArrayList(arrayListOf(DataEditText(ed_account_name_profile, username!!)
                , DataEditText(ed_account_pass_profile, ed_account_pass_profile.text.toString())
                , DataEditText(ed_re_account_password_profile, ed_re_account_password_profile.text.toString())
                , DataEditText(ed_email_profile, ed_email_profile.text.toString())
                , DataEditText(ed_birth_day, ed_birth_day.text.toString())))

        editTableChangeText = EditableChangeText(context, listItemEditText, callBackEnable = { status: Boolean ->
            if (status) {
                tv_record_profile.setBackgroundResource(R.drawable.custom_back_ground_accept)
            } else {
                if (!stateClickable) {
                    tv_record_profile.setBackgroundResource(R.drawable.ripple_record_un_save)
                }
            }
        })

        editTableChangeText.addOnChangeTextListener()
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        userProfileManager.callBackManager = { key: String, userProfile: ExtendedProfileUserModel? ->
            updateChild = key
            calendarBirthDay = Calendar.getInstance()
            calendarBirthDay.timeInMillis = userProfile?.dateUser!!
            ed_birth_day.setText("${calendarBirthDay.get(Calendar.DAY_OF_MONTH)}/${calendarBirthDay.get(Calendar.MONTH)}/${calendarBirthDay.get(Calendar.YEAR)}")

            when (userProfile.gender) {
                0 -> {
                    enableMale()
                }
                1 -> {
                    disableFemale()
                }
            }
            addItemEditText()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_PHOTO ->
                if (data != null) {
                    changeProfileUser.onUpdatePhotoUser(data.data)
                    tv_show_upload.visibility = View.GONE
                } else {
                    tv_show_upload.visibility = View.GONE
                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity.supportFinishAfterTransition()
            }
        }
        return true
    }

    companion object {
        private const val REQUEST_PHOTO: Int = 1111
        fun newInstance(): ProfileUserFragment = ProfileUserFragment().apply { arguments = Bundle() }
    }
}

