package com.ipati.dev.castleevent.base


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import android.support.v4.net.ConnectivityManagerCompat
import android.support.v7.app.AppCompatActivity
import com.ipati.dev.castleevent.LoadingChangeLanguage


open class BaseAppCompatActivity : AppCompatActivity() {
    private lateinit var handler: Handler

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocalHelper().onAttach(newBase))
        overridePendingTransition(0, android.R.anim.fade_out)
    }

    fun setLanguage(language: String) {
        LocalHelper().setLocal(this, language)

        val changeLanguageIntent = Intent(this, LoadingChangeLanguage::class.java)
        changeLanguageIntent.putExtra("changeLanguage", "ระบบกำลังเปลี่ยนภาษา...")
        startActivity(changeLanguageIntent)

        handler = Handler()
        handler.postDelayed({
            this.recreate()
        }, 100)

    }

    fun setStateNetWorking(stateNetWork: ((state: Boolean) -> Unit)) {
        val manager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeInfo: NetworkInfo? = manager.activeNetworkInfo
        activeInfo?.let {
            when (activeInfo.type) {
                ConnectivityManager.TYPE_MOBILE -> {
                    if (activeInfo.isConnectedOrConnecting) {
                        stateNetWork(true)
                    } else {
                        stateNetWork(false)
                    }
                }

                ConnectivityManager.TYPE_WIFI -> {
                    if (activeInfo.isConnectedOrConnecting) {
                        stateNetWork(true)
                    } else {
                        stateNetWork(false)
                    }
                }
                else -> {
                    stateNetWork(false)
                }
            }
        } ?: stateNetWork(false)
    }
}
