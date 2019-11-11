package com.example.fitfactory.presentation.fragments.signUp

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fitfactory.R
import com.example.fitfactory.data.models.request.SignUpRequest
import com.example.fitfactory.data.models.response.BaseResponse
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.presentation.fragments.signIn.ErrorSignIn
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUpFragment : BaseFragment() {

    private val viewModel by lazy { SignUpViewModel() }
    private var animationList = ArrayList<AnimatedVectorDrawable>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        viewModel.callResult.observe(viewLifecycleOwner, Observer {
            resetAnimations()
            when (it) {
                is BaseResponse -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    signUpFragment_signUp.setImageDrawable(
                        ContextCompat.getDrawable(
                            context!!,
                            if (it.status) R.drawable.load_button_endanim_positive else R.drawable.load_button_endanim_negative
                        )
                    )
                }
                is ErrorSignIn -> {
                    signUpFragment_signUp.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.load_button_endanim_negative))
                    Toast.makeText(context, it.message ?: "Błąd", Toast.LENGTH_SHORT).show()
                }
            }
//            animateView(signUpFragment_signUp.drawable)
        })
    }

    private fun setListeners() {
        signUpFragment_signUp.setOnClickListener { v ->
            animateView((v as ImageView).drawable)
            signUpFragment_label.visibility = View.INVISIBLE
            viewModel.signUp(
                SignUpRequest(
                    username = signUpFragment_userName.text.toString(),
                    email = signUpFragment_userEmail.text.toString(),
                    password = signUpFragment_userPassword.text.toString()
                )

            )
        }
    }

    private fun resetAnimations() {
        animationList.forEach { drawable ->
            drawable.stop()
        }
        animationList.clear()
    }

    private fun animateView(drawable: Drawable) {
        (drawable as? AnimatedVectorDrawable)?.let {
            it.start()
            animationList.add(it)
        }
    }

    override fun onDestroyView() {
        resetAnimations()
        super.onDestroyView()
    }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = false
    override fun topBarTitle() = null
    override fun topBarEnabled() = false
    override fun backButtonEnabled(): Boolean = true

}
