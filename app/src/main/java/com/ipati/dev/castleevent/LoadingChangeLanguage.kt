package com.ipati.dev.castleevent

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.ipati.dev.castleevent.base.BaseAppCompatActivity
import com.ipati.dev.castleevent.extension.pxToDp
import kotlinx.android.synthetic.main.activity_loading_change_language.*

class LoadingChangeLanguage : BaseAppCompatActivity() {
    private lateinit var handlerThread: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_loading_change_language)

        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setBackgroundDrawableResource(android.R.color.transparent)

        overridePendingTransition(0, android.R.anim.fade_out)
        loadLanguage()
    }

    private fun loadLanguage() {
        tv_header_change_language.text = intent.getStringExtra("changeLanguage")

        lottie_view_animation_loading_change_language.layoutParams.height = applicationContext.pxToDp(350)
        lottie_view_animation_loading_change_language.setAnimation("loading_animation.json")
        lottie_view_animation_loading_change_language.loop(true)
        lottie_view_animation_loading_change_language.playAnimation()

        handlerThread = Handler()
        handlerThread.postDelayed({
            finish()
        }, 1000)
    }
}
