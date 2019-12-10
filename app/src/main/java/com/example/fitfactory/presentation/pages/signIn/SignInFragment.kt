package com.example.fitfactory.presentation.pages.signIn

import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fitfactory.R
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.utils.SpanTextUtil
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : BaseFragment() {

    private val viewModel by lazy { SignInViewModel() }
    private var animateList = ArrayList<AnimatedVectorDrawable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        viewModel.callResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SuccessSignIn -> findNavController().navigate(R.id.mapFragment)
                is CancelSignIn -> {
                }
                is ErrorSignIn -> {
                    Toast.makeText(context, it.message ?: "Błąd", Toast.LENGTH_SHORT).show()
                }
            }
            resetAnimations()
        })
    }

    private fun setListeners() {
        facebookSignIn.setOnClickListener { v ->
            animateView(v)
            viewModel.facebookSignIn(this)
        }

        googleSignIn.setOnClickListener { v ->
            animateView(v)
            viewModel.googleSignIn(this)
        }
        signIn.setOnClickListener { v ->
            //            if (viewModel.validate(userName, userPassword)){
            animateView(v)
            viewModel.defaultSignIn(userName.text.toString(), userPassword.text.toString())
//            }
        }

        context?.let {
            SpanTextUtil(it).setClickableSpanOnTextView(
                signUp,
                getString(R.string.joinUs),
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        findNavController().navigate(R.id.signUpFragment)
                    }
                },
                R.color.silverLight
            )
            SpanTextUtil(it).setClickableSpanOnTextView(
                forgotPassword,
                getString(R.string.remember_password),
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        findNavController().navigate(R.id.rememberPasswordFragment)
                    }
                },
                R.color.silverLight
            )
        }
    }


    private fun animateView(view: View) {
        when (view) {
            facebookSignIn -> {
                googleSignIn.visibility = View.GONE
                signIn.visibility = View.GONE
            }
            googleSignIn -> {
                facebookSignIn.visibility = View.GONE
                signIn.visibility = View.GONE
            }
            signIn -> {
                googleSignIn.visibility = View.GONE
                facebookSignIn.visibility = View.GONE
            }
        }

        (view.background as? AnimatedVectorDrawable)?.let {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = false
    override fun topBarTitle() = null
    override fun topBarEnabled() = false
    override fun backButtonEnabled(): Boolean = true
}
