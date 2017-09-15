package com.ipati.dev.castleevent.fragment.loading

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.GenerateQrCode
import com.ipati.dev.castleevent.model.Glide.loadPhotoUserTickets
import kotlinx.android.synthetic.main.activity_tickets_event_dialog_fragment.*

class TicketsEventDialogFragment : DialogFragment() {
    private lateinit var mGenerateQrCode: GenerateQrCode
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGenerateQrCode = GenerateQrCode(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_tickets_event_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        initialInformationDialog()
    }

    private fun initialInformationDialog() {
        loadPhotoUserTickets(context, arguments.getString("userPhoto"), im_photo_user)
        tv_user_name_ticket.text = arguments.getString("userAccount")
        tv_count_people_tickets_dialog.text = arguments.getLong("count").toString()
        tv_location_tickets_dialog.text = arguments.getString("eventLocation")
        tv_name_event_dialog.text = arguments.getString("eventName")

        tv_close_dialog_qr_code.setOnClickListener {
            dialog.dismiss()
        }

        im_qr_code.setImageBitmap(mGenerateQrCode.bitMapQrCode(arguments.getString("userAccount"), arguments.getLong("count").toString()))
    }

    companion object {
        fun newInstance(eventId: String, userPhoto: String?, eventName: String, eventLogo: String, userAccount: String, eventLocation: String, count: Long): TicketsEventDialogFragment {
            return TicketsEventDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("eventId", eventId)
                    putString("userPhoto", userPhoto)
                    putString("eventLogo", eventLogo)
                    putString("userAccount", userAccount)
                    putString("eventName", eventName)
                    putString("eventLocation", eventLocation)
                    putLong("count", count)
                }
            }
        }
    }
}
