package com.ipati.dev.castleevent.fragment

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.ipati.dev.castleevent.LoginActivity
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.RegisterActivity
import com.ipati.dev.castleevent.authCredential.facebookAuthCredential
import com.ipati.dev.castleevent.authCredential.twitterAuthCredential
import com.ipati.dev.castleevent.service.*
import com.ipati.dev.castleevent.utill.SharePreferenceGoogleSignInManager
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import kotlinx.android.synthetic.main.activity_login_fragment.*


class LoginFragment : Fragment(), View.OnClickListener, LifecycleRegistryOwner {

    private lateinit var callbackManager: CallbackManager
    private lateinit var loginTwitterAuthentication: TwitterAuthClient
    private lateinit var twitterConfig: TwitterConfig
    private lateinit var mGoogleSharePreference: SharePreferenceGoogleSignInManager
    private lateinit var mLoginAuthManager: LoginAuthManager

    private var mRegistry: LifecycleRegistry = LifecycleRegistry(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGoogleSharePreference = SharePreferenceGoogleSignInManager(context)
        mLoginAuthManager = LoginAuthManager(context, lifecycle)
        callbackManager = CallbackManager.Factory.create()

        twitterConfig()
        facebookLoginManager()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_login_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        im_facebook.setOnClickListener { v -> onClick(v) }
        im_twitter.setOnClickListener { v -> onClick(v) }
        im_google_plus.setOnClickListener { v -> onClick(v) }
        tv_create_account_login.setOnClickListener { v -> onClick(v) }
        tv_login_fragment.setOnClickListener { v -> onClick(v) }
    }

    private fun facebookLoginManager() {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                facebookAuthCredential(activity, result?.accessToken!!)
            }

            override fun onCancel() {
                Toast.makeText(activity, cancelMsg, Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(activity, error?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun twitterConfig() {
        twitterConfig = TwitterConfig.Builder(this.context)
                .logger(DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(TwitterAuthConfig("DsQpZuWxVXdNeii1rMy98rjSp"
                        , "icMqh1zfD9vhs3uRslXaneRMR4zWIHTeXgT4AnB5rqqaCUs3KF"))
                .debug(true)
                .build()
        Twitter.initialize(twitterConfig)
        loginTwitterAuthentication = TwitterAuthClient()
    }


    private fun TwitterLoginManager() {
        loginTwitterAuthentication.authorize(this.activity, object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>?) {
                result?.let {
                    twitterAuthCredential(activity, result.data)
                }
            }

            override fun failure(exception: TwitterException?) {
                Toast.makeText(context, exception?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_facebook -> LoginFacebook(this)
            R.id.im_twitter -> TwitterLoginManager()
            R.id.im_google_plus -> loginGoogleSignInOption(activity)
            R.id.tv_create_account_login -> {
                val registerIntent = Intent(context, RegisterActivity::class.java)
                startActivity(registerIntent)
            }
            R.id.tv_login_fragment -> {
                when {
                    TextUtils.isEmpty(login_ed_username.text.toString()) -> {
                        login_ed_username.error = "Please put your userEmail"
                    }
                }

                when {
                    TextUtils.isEmpty(login_ed_password.text.toString()) -> {
                        login_ed_password.error = "Please put your password"
                    }
                }

                when {
                    !TextUtils.isEmpty(login_ed_username.text.toString()) && !TextUtils.isEmpty(login_ed_password.text.toString()) -> {
                        when (true) {
                            android.util.Patterns.EMAIL_ADDRESS.matcher(login_ed_username.text.toString()).matches() -> {
                                when (login_ed_password.length()) {
                                    in 6..10 -> {
                                        mLoginAuthManager.loginAuthentication(login_ed_username.text.toString(), login_ed_password.text.toString(), login_ed_username, login_ed_password)
                                    }
                                    else -> {
                                        login_ed_password.error = "Password is More  6 Character"
                                    }
                                }
                            }
                            else -> {
                                login_ed_username.error = "Please put @ in your userEmail"
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SignInGoogle -> {
                val mGoogleSignInResult: GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                if (mGoogleSignInResult.isSuccess) {
                    val mGoogleSignInAccount: GoogleSignInAccount = mGoogleSignInResult.signInAccount!!
                    callbackGoogleSignIn(activity, mGoogleSignInAccount, mGoogleSharePreference)
                } else {
                    Toast.makeText(context, mGoogleSignInResult.status.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            else -> {
                callbackManager.onActivityResult(requestCode, resultCode, data)
                loginTwitterAuthentication.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun getLifecycle(): LifecycleRegistry {
        return mRegistry
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

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
