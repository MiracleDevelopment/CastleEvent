package com.ipati.dev.castleevent.fragment.loading

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ipati.dev.castleevent.FavoriteCategoryActivity
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.UserManager.*
import kotlinx.android.synthetic.main.activity_register_dialog_fragment.*


class RegisterDialogFragment : DialogFragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.activity_register_dialog_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.attributes.windowAnimations = android.R.anim.anticipate_overshoot_interpolator
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        tv_register_dialog_male.setOnClickListener(this)
        tv_register_dialog_female.setOnClickListener(this)
        tv_next_step_gender.setOnClickListener(this)
        defaultEnable()
    }

    private fun defaultEnable() {
        tv_register_dialog_male.isActivated = true
        gender = 0
    }

    private fun enableMale() {
        tv_register_dialog_male.isActivated = true
        tv_register_dialog_female.isActivated = false
    }

    private fun enableFemale() {
        tv_register_dialog_male.isActivated = false
        tv_register_dialog_female.isActivated = true
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_register_dialog_male -> {
                enableMale()
                gender = 0
            }

            R.id.tv_register_dialog_female -> {
                enableFemale()
                gender = 1
            }

            R.id.tv_next_step_gender -> {
                val refRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
                val refUserProfile: DatabaseReference = refRoot.child("userProfile").child(uidRegister)
                val modelProfile = ExtendedProfileUserModel(gender, birthDay, phoneNumber!!)
                refUserProfile.push().setValue(modelProfile).addOnCompleteListener { task: Task<Void> ->
                    when {
                        task.isSuccessful -> {
                            val intentFavoriteCategory = Intent(context, FavoriteCategoryActivity::class.java)
                            intentFavoriteCategory.putExtra("status", true)
                            startActivity(intentFavoriteCategory)

                            dialog.dismiss()
                            activity.supportFinishAfterTransition()
                        }
                        else -> {
                            Toast.makeText(context, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
        }
    }

    companion object {
        fun newInstance(): RegisterDialogFragment {
            return RegisterDialogFragment().apply {
                arguments = Bundle()
            }
        }
    }

}
