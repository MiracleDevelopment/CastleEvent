package com.ipati.dev.castleevent.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.authCredential.facebookAuthCredential
import com.ipati.dev.castleevent.service.*
import com.twitter.sdk.android.core.Twitter
import kotlinx.android.synthetic.main.activity_login_fragment.*


class LoginFragment : Fragment(), View.OnClickListener {
    private lateinit var callbackManager: CallbackManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbackManager = CallbackManager.Factory.create()
        facebookLoginManager()
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

    private fun facebookLoginManager() {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                facebookAuthCredential(loadingListener, activity, result?.accessToken!!)
            }

            override fun onCancel() {
                loadingListener?.onHindLoading(false)
                Toast.makeText(activity, cancelMsg, Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException?) {
                loadingListener?.onHindLoading(false)
                Toast.makeText(activity, error?.message.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_facebook -> LoginFacebook(context, this)
            R.id.im_twitter -> loginTwitter(activity)
            R.id.im_google_plus -> loginGoogleSignInDialog(activity)
        }
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
        googleApiService(activity)?.disconnect()
    }

    companion object {
        private var listEventObject: String = "LoginActivity"
        fun newInstance(Object: String): LoginFragment {
            val loginFragment = LoginFragment()
            val bundle = Bundle()
            bundle.putString(listEventObject, Object)
            loginFragment.arguments = bundle
            return loginFragment
        }
    }
}
