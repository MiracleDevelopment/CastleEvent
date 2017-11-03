package com.ipati.dev.castleevent.fragment.loading

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.GenerateQrCode
import com.ipati.dev.castleevent.model.Fresco.loadPhotoUserTickets
import kotlinx.android.synthetic.main.activity_tickets_event_dialog_fragment.*

class TicketsEventDialogFragment : DialogFragment() {
    private lateinit var generateQrCode: GenerateQrCode
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        generateQrCode = GenerateQrCode(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_tickets_event_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initialInformationDialog()
    }

    @SuppressLint("SetTextI18n")
    private fun initialInformationDialog() {
        loadPhotoUserTickets(context, arguments.getString("eventPhoto"), im_logo_event_ticket_dialog)
        tv_username_tickets_dialog.text = arguments.getString("userAccount")
        tv_people_count_tickets_dialog.text = "${arguments.getLong("count")} ${getString(R.string.countPeopleTickets)}"
        tv_name_event_tickets_dialog.text = arguments.getString("eventName")
        tv_close_dialog_qr_code.setOnClickListener {
            dialog.dismiss()
        }

        im_qr_code.setImageBitmap(generateQrCode.bitMapQrCode(arguments.getString("userAccount")
                , arguments.getLong("count").toString()))
    }

    companion object {
        fun newInstance(eventId: String, eventPhoto: String?, eventName: String, userAccount: String, eventLocation: String, count: Long): TicketsEventDialogFragment {
            return TicketsEventDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("eventId", eventId)
                    putString("eventPhoto", eventPhoto)
                    putString("userAccount", userAccount)
                    putString("eventName", eventName)
                    putString("eventLocation", eventLocation)
                    putLong("count", count)
                }
            }
        }
    }
}
