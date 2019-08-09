package com.example.fitfactory.presentation.fragments.signInFragment

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.fitfactory.R
import kotlinx.android.synthetic.main.sign_in_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignInFragment : Fragment() {

    private lateinit var viewModel: SignInViewModel
    private var animateList = ArrayList<AnimatedVectorDrawable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
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
            moveToMapFragment(v)
        }
        signInFragment_googleSignIn.setOnClickListener { v ->
            animateView(signInFragment_logo.drawable)
            animateView((v as ImageView).background)
            signInFragment_facebookSignIn.visibility = View.GONE
            signInFragment_signIn.visibility = View.GONE
            moveToMapFragment(v)
        }
        signInFragment_signIn.setOnClickListener { v ->
            animateView(signInFragment_logo.drawable)
            animateView((v as ImageView).background)
            signInFragment_googleSignIn.visibility = View.GONE
            signInFragment_facebookSignIn.visibility = View.GONE
            moveToMapFragment(v)
        }

        signInFragment_signUp.setOnClickListener { findNavController().navigate(R.id.signUpFragment) }
        signInFragment_forgotPassword.setOnClickListener { findNavController().navigate(R.id.rememberPasswordFragment) }
    }

    private fun moveToMapFragment(view: ImageView){
        MainScope().launch {
            delay(4000)
            findNavController().navigate(R.id.mainFragment)
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
            }else{
                drawable.stop()
            }
        }
        animateList.clear()

        signInFragment_googleSignIn.visibility = View.VISIBLE
        signInFragment_facebookSignIn.visibility = View.VISIBLE
        signInFragment_signIn.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        resetAnimations()
        super.onDestroyView()
    }
}
