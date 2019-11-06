package com.example.fitfactory.presentation.fragments.signUp

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUpFragment : BaseFragment() {

    private lateinit var viewModel: SignUpViewModel
    private var animationList = ArrayList<AnimatedVectorDrawable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        signUpFragment_signUp.setOnClickListener { v ->
            animateView((v as ImageView).drawable)
            signUpFragment_label.visibility = View.INVISIBLE
            GlobalScope.launch {
                delay(4000)
                resetAnimations()
                signUpFragment_signUp.setImageDrawable(resources.getDrawable(R.drawable.load_button_endanim_positive))
                animateView(v.drawable)
            }
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
