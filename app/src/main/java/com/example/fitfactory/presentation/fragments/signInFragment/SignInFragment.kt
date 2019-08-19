package com.example.fitfactory.presentation.fragments.signInFragment

import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
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
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.Profile
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.sign_in_fragment.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class SignInFragment : Fragment() {

    @Inject
    lateinit var user: User

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
        return inflater.inflate(R.layout.sign_in_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        signInFragment_facebookSignIn.setOnClickListener { v ->
            animateView(signInFragment_logo.drawable)
            animateView((v as ImageView).background)
            signInFragment_googleSignIn.visibility = View.GONE
            signInFragment_signIn.visibility = View.GONE
            registerFacebookCallback()
            LoginManager.getInstance().logInWithReadPermissions(this, mutableListOf("public_profile"))
        }

        signInFragment_googleSignIn.setOnClickListener { v ->
            animateView(signInFragment_logo.drawable)
            animateView((v as ImageView).background)
            signInFragment_facebookSignIn.visibility = View.GONE
            signInFragment_signIn.visibility = View.GONE
            moveToMapFragment()
        }
        signInFragment_signIn.setOnClickListener { v ->
            animateView(signInFragment_logo.drawable)
            animateView((v as ImageView).background)
            signInFragment_googleSignIn.visibility = View.GONE
            signInFragment_facebookSignIn.visibility = View.GONE
            moveToMapFragment()
        }

        signInFragment_signUp.setOnClickListener { findNavController().navigate(R.id.signUpFragment) }
        signInFragment_forgotPassword.setOnClickListener { findNavController().navigate(R.id.rememberPasswordFragment) }
    }

    private fun moveToMapFragment() {
        MainScope().launch {
            delay(1000)
            val mainActivity = Intent(activity, MainActivity::class.java)
            mainActivity.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(mainActivity)
            activity?.finish()
        }
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

        signInFragment_googleSignIn.visibility = View.VISIBLE
        signInFragment_facebookSignIn.visibility = View.VISIBLE
        signInFragment_signIn.visibility = View.VISIBLE
    }

    private fun registerFacebookCallback(){
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                val profile = Profile.getCurrentProfile()
                user.firstName = profile.firstName
                user.lastName = profile.lastName
                user.picture = profile.getProfilePictureUri(300, 300).toString()
                moveToMapFragment()
            }

            override fun onCancel() {
                resetAnimations()
                Toast.makeText(context,"Cancel", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(context,"Błąd logowania", Toast.LENGTH_SHORT).show()
                resetAnimations()
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        resetAnimations()
    }
}
