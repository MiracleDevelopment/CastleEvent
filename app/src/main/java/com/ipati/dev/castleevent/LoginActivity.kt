package com.ipati.dev.castleevent

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.ipati.dev.castleevent.fragment.LoginFragment
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import com.ipati.dev.castleevent.base.BaseAppCompatActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : BaseAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.frame_login_fragment
                , LoginFragment.newInstance("LoginFragment"), tagLoginFragment).commitNow()

        Log.d("hasKeyFacebook", initHashKey(context = applicationContext))
    }

    private fun initHashKey(context: Context): String {
        try {
            val info = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                return String(Base64.encode(md.digest(), 0)).trim({ it <= ' ' })
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return ""
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return ""
        }
        return ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment: Fragment? = supportFragmentManager.findFragmentByTag(tagLoginFragment)
        fragment?.let {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        private const val tagLoginFragment = "LoginFragment"
    }
}
