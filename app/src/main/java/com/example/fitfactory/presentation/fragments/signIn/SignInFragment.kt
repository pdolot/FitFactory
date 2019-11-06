package com.example.fitfactory.presentation.fragments.signIn

import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.fitfactory.R
import com.example.fitfactory.data.models.User
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.activities.mainActivity.MainActivity
import com.example.fitfactory.constants.Constants
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.utils.SpanTextUtil
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.Profile
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInFragment : BaseFragment() {

    @Inject
    lateinit var user: User

    @Inject
    lateinit var googleClient: GoogleSignInClient

    private lateinit var viewModel: SignInViewModel
    private var animateList = ArrayList<AnimatedVectorDrawable>()

    // Facebook
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        callbackManager = CallbackManager.Factory.create()
        Injector.component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        facebookSignIn.setOnClickListener { v ->
            animateView(logo.drawable)
            animateView((v as ImageView).background)
            googleSignIn.visibility = View.GONE
            signIn.visibility = View.GONE
            registerFacebookCallback()
            LoginManager.getInstance().logInWithReadPermissions(this, mutableListOf("public_profile"))
        }

        googleSignIn.setOnClickListener { v ->
            animateView(logo.drawable)
            animateView((v as ImageView).background)
            facebookSignIn.visibility = View.GONE
            signIn.visibility = View.GONE
            googleSignIn()
//            moveToMapFragment()
        }
        signIn.setOnClickListener { v ->
            animateView(logo.drawable)
            animateView((v as ImageView).background)
            googleSignIn.visibility = View.GONE
            facebookSignIn.visibility = View.GONE
            findNavController().navigate(R.id.mapFragment)
        }

        context?.let {
            SpanTextUtil(it).
                setClickableSpanOnTextView(signUp, getString(R.string.joinUs), object : ClickableSpan(){
                    override fun onClick(widget: View) {
                        findNavController().navigate(R.id.signUpFragment)
                    }
                }, R.color.silverLight)
            SpanTextUtil(it).setClickableSpanOnTextView(forgotPassword, getString(R.string.remember_password), object : ClickableSpan(){
                override fun onClick(widget: View) {
                    findNavController().navigate(R.id.rememberPasswordFragment)
                }
            }, R.color.silverLight)
        }
//        signInFragment_signUp.setOnClickListener { findNavController().navigate(R.id.signUpFragment) }
//        signInFragment_forgotPassword.setOnClickListener { findNavController().navigate(R.id.rememberPasswordFragment) }
    }

    private fun googleSignIn() {
        startActivityForResult(googleClient.signInIntent, Constants.GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    private fun animateView(drawable: Drawable) {
        (drawable as? AnimatedVectorDrawable)?.let {
            it.start()
            animateList.add(it)
        }
    }

    private fun resetAnimations() {
        animateList.forEach { drawable ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                drawable.reset()
            } else {
                drawable.stop()
            }
        }
        animateList.clear()

        googleSignIn.visibility = View.VISIBLE
        facebookSignIn.visibility = View.VISIBLE
        signIn.visibility = View.VISIBLE
    }

    private fun registerFacebookCallback() {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                val profile = Profile.getCurrentProfile()
                user.firstName = profile.firstName
                user.lastName = profile.lastName
                user.picture = profile.getProfilePictureUri(300, 300).toString()
                findNavController().navigate(R.id.mapFragment)
            }

            override fun onCancel() {
                resetAnimations()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(context, getString(R.string.sign_in_failed), Toast.LENGTH_SHORT).show()
                resetAnimations()
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            Constants.GOOGLE_SIGN_IN_REQUEST_CODE -> {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            account?.let {
                user.firstName = it.givenName ?: ""
                user.lastName = it.familyName ?: ""
                user.picture = it.photoUrl.toString()
                findNavController().navigate(R.id.mapFragment)
            }
        } catch (e: ApiException) {
            Log.e("SignInGoogle", "Sign in failed code = ${e.statusCode}")
            Toast.makeText(context, getString(R.string.sign_in_failed), Toast.LENGTH_SHORT).show()
            resetAnimations()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        resetAnimations()
    }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = false
    override fun topBarTitle() = null
    override fun topBarEnabled() = false
    override fun backButtonEnabled(): Boolean = true
}
