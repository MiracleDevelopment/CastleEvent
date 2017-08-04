package com.ipati.dev.castleevent.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.Auth
import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.StatedFragment
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.service.*
import com.twitter.sdk.android.core.Twitter
import kotlinx.android.synthetic.main.activity_login_fragment.*


class LoginFragment : StatedFragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_facebook -> LoginFacebook(activity)
            R.id.im_twitter -> loginTwitter(activity)
            R.id.im_google_plus -> loginGoogleSignInDialog(activity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CallbackManagerFacebook()
        RegisterCallbackFacebook(activity, callbackManager)
        Twitter.initialize(activity)
        twitterConfig(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_login_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        im_facebook.setOnClickListener { v -> onClick(v) }
        im_twitter.setOnClickListener { v -> onClick(v) }
        im_google_plus.setOnClickListener { v -> onClick(v) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SignInGoogle -> callbackGoogleSignIn(activity, Auth.GoogleSignInApi.getSignInResultFromIntent(data))
            else -> {
                callbackManager.onActivityResult(requestCode, resultCode, data)
                twitterLoginAuthentication.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        googleApiService(activity)?.connect()
    }

    override fun onStop() {
        super.onStop()
        loginManager.logOut()
        googleApiService(activity)?.disconnect()
    }

    companion object {
        var LoginFragmentKey: String = "LoginActivity"
        fun newInstance(Object: String): LoginFragment {
            val loginFragment: LoginFragment = LoginFragment()
            val bundle: Bundle = Bundle()
            bundle.putString(LoginFragmentKey, Object)
            loginFragment.arguments = bundle
            return loginFragment
        }
    }
}
