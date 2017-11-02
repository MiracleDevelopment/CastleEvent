package com.ipati.dev.castleevent.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.*
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.extension.toStrEditText
import com.ipati.dev.castleevent.model.UserProfileUpdate
import kotlinx.android.synthetic.main.activity_change_custom_profile_dialog_fragment.*
import kotlinx.android.synthetic.main.activity_profile_user_fragment.*

class ChangeCustomProfileDialogFragment : DialogFragment() {
    private var RequestUsername: Int = 1001
    private var RequestPassword: Int = 1002
    private var RequestEmail: Int = 1003

    private lateinit var userChangeProfile: UserProfileUpdate

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_change_custom_profile_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        userChangeProfile = UserProfileUpdate(context, tv_input_username_profile, tv_input_password_profile, tv_input_re_password, tv_input_email_profile, activity)

        requestCodeObject()

//        ed_record_profile.setOnClickListener { viewRecord -> onClick(viewRecord) }
//        tv_cancel_dialog_profile.setOnClickListener { viewCanCel: View -> onClick(viewCanCel) }
    }

    private fun requestCodeObject() {
        when (arguments.getInt(codeObject)) {
            RequestUsername -> {
                tv_input_confirm_password.visibility = View.GONE

                initialEdiText(arguments.getString(titleObject)
                        , arguments.getString(msgObject)
                        , InputType.TYPE_CLASS_TEXT)
            }

            RequestPassword -> {
                tv_input_confirm_password.visibility = View.VISIBLE
                ed_confirm_password.hint = "Re-Password"

                initialEdiText(arguments.getString(titleObject)
                        , arguments.getString(msgObject)
                        , InputType.TYPE_TEXT_VARIATION_PASSWORD)
            }

            RequestEmail -> {
                tv_input_confirm_password.visibility = View.GONE

                initialEdiText(arguments.getString(titleObject)
                        , arguments.getString(msgObject)
                        , InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)

            }
        }
    }

    private fun initialEdiText(title: String, msg: String, inputType: Int) {
        ed_input_edit.hint = title
        ed_input_edit.inputType = inputType

        if (tv_input_confirm_password.visibility == View.GONE) {
            ed_input_edit.transformationMethod = null
        } else {
            ed_input_edit.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        ed_input_edit.setText(msg)
    }


    companion object {
        private const val titleObject = "title"
        private const val msgObject = "msg"
        private const val codeObject = "code"
        fun newInstance(title: String, msg: String, code: Int): ChangeCustomProfileDialogFragment {
            return ChangeCustomProfileDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(titleObject, title)
                    putString(msgObject, msg)
                    putInt(codeObject, code)
                }
            }
        }
    }
}
