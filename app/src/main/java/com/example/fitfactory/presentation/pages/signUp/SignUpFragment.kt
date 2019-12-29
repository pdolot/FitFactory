package com.example.fitfactory.presentation.pages.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.fitfactory.R
import com.example.fitfactory.data.models.request.SignUpRequest
import com.example.fitfactory.data.models.response.BaseResponse
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.presentation.pages.signIn.ErrorSignIn
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : BaseFragment() {

    private val viewModel by lazy { SignUpViewModel() }

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
            when (it) {
                is BaseResponse -> {
                    if (it.status) {
                        signUpFragment_signUp.onSuccess("ZAREJESTROWANO")
                    } else {
                        signUpFragment_signUp.onError("UŻYTKOWNIK ISTNIEJE")
                    }
                }
                is ErrorSignIn -> {
                    signUpFragment_signUp.onError("Błąd połączenia")
                }
            }
            signUpFragment_signUp.stop()
        })
    }

    private fun setListeners() {
        signUpFragment_signUp.setOnClickListener { v ->
            if (viewModel.validate(
                    signUpFragment_userName,
                    signUpFragment_userEmail,
                    signUpFragment_userPassword,
                    signUpFragment_userConfirmPassword
                ) && signUpFragment_regulationsCheckbox.isChecked
            ) {
                signUpFragment_signUp.startAnim()
                viewModel.signUp(
                    SignUpRequest(
                        username = signUpFragment_userName.text.toString().trim(),
                        email = signUpFragment_userEmail.text.toString().trim(),
                        password = signUpFragment_userPassword.text.toString().trim()
                    )

                )
            }
        }

        listOf(signUpFragment_userName, signUpFragment_userEmail, signUpFragment_userPassword, signUpFragment_userConfirmPassword).forEach {
            it.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) signUpFragment_signUp.reset()
            }
        }
    }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = false
    override fun topBarTitle() = null
    override fun topBarEnabled() = false
    override fun backButtonEnabled(): Boolean = true

}
