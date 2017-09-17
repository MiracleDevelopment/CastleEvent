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

class ChangeCustomProfileDialogFragment : DialogFragment(), View.OnClickListener {
    private var RequestUsername: Int = 1001
    private var RequestPassword: Int = 1002
    private var RequestEmail: Int = 1003

    private lateinit var mUserChangeProfile: UserProfileUpdate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUserChangeProfile = UserProfileUpdate(context, activity)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_change_custom_profile_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestCodeObject()

        ed_record_profile.setOnClickListener { viewRecord -> onClick(viewRecord) }
        tv_cancel_dialog_profile.setOnClickListener { viewCanCel: View -> onClick(viewCanCel) }
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

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ed_record_profile -> {
                when (arguments.getInt(codeObject)) {
                    RequestUsername -> {
                        mUserChangeProfile.onValidateUsername(ed_input_edit.toStrEditText())
                        when {
                            mUserChangeProfile.statusUsername() == "Success" -> dialog.dismiss()
                            else -> {
                                ed_input_edit.error = mUserChangeProfile.statusUsername()
                            }
                        }
                    }

                    RequestPassword -> {
                        mUserChangeProfile.onValidatePassword(ed_input_edit.toStrEditText()
                                , ed_confirm_password.toStrEditText())

                        if (mUserChangeProfile.statusPassword() == "Success") {
                            dialog.dismiss()
                        }

                        if (mUserChangeProfile.statusPassword() == "Missing Not Match") {
                            ed_input_edit.error = mUserChangeProfile.statusPassword()
                            ed_confirm_password.error = mUserChangeProfile.statusPasswordConfirm()
                        }

                        if (mUserChangeProfile.statusPassword() == "isEmpty") {
                            ed_input_edit.error = mUserChangeProfile.statusPassword()
                        }

                        if (mUserChangeProfile.statusPasswordConfirm() == "isEmpty") {
                            ed_confirm_password.error = mUserChangeProfile.statusPasswordConfirm()
                        }

                        if (mUserChangeProfile.statusPassword() == "More 6 Character") {
                            ed_input_edit.error = mUserChangeProfile.statusPassword()
                        }

                        if (mUserChangeProfile.statusPasswordConfirm() == "More 6 Character") {
                            ed_confirm_password.error = mUserChangeProfile.statusPasswordConfirm()
                        }
                    }

                    RequestEmail -> {
                        mUserChangeProfile.onValidateEmail(ed_input_edit.toStrEditText())
                        when {
                            mUserChangeProfile.statusEmail() == "Success" -> dialog.dismiss()
                            mUserChangeProfile.statusEmail() == "isEmpty" -> {
                                ed_input_edit.error = mUserChangeProfile.statusEmail()
                            }
                            else -> {
                                ed_input_edit.error = mUserChangeProfile.statusEmail()
                            }
                        }
                    }
                }
            }

            R.id.tv_cancel_dialog_profile -> {
                dialog.dismiss()
            }
        }
    }

    companion object {
        private var titleObject = "title"
        private var msgObject = "msg"
        private var codeObject = "code"
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
