package com.ipati.dev.castleevent.base


import android.content.Context
import android.support.v7.app.AppCompatActivity


open class BaseAppCompatActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocalHelper().onAttach(newBase))
    }


    fun setLanguage(language: String) {
        LocalHelper().setLocal(this, language)
        this.recreate()
    }

}
