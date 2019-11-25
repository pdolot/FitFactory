package com.example.fitfactory.presentation.pages.signUp

import android.annotation.SuppressLint
import android.content.Context
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfactory.data.models.request.SignUpRequest
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.pages.signIn.ErrorSignIn
import com.example.fitfactory.utils.Validator
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class SignUpViewModel : ViewModel() {
    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    @Inject
    lateinit var context: Context

    init {
        Injector.component.inject(this)
    }

    var callResult = MutableLiveData<Any>()

    @SuppressLint("CheckResult")
    fun signUp(user: SignUpRequest){
        retrofitRepository.signUp(user)
            .subscribeBy(
                onSuccess = {
                    if (it.status){
                        callResult.postValue(it)
                    }else{
                        callResult.postValue(ErrorSignIn(it.message))
                    }
                },
                onError = {
                    callResult.postValue(ErrorSignIn(it.message))
                }
            )
    }

    fun validate(username: EditText, userEmail: EditText, password: EditText, confirmPassword: EditText): Boolean{
        if (!Validator(username).validateTextField()) return false
        if (!Validator(userEmail).validateEmail()) return false
        if (!Validator(password).validatePassword()) return false
        if (password.text.toString() != confirmPassword.text.toString()){
            Toast.makeText(context, "Hasła nie są takie same", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
