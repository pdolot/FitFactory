package com.example.fitfactory.presentation.fragments.signUp

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfactory.data.models.request.SignUpRequest
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.fragments.signIn.ErrorSignIn
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SignUpViewModel : ViewModel() {
    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    init {
        Injector.component.inject(this)
    }

    var callResult = MutableLiveData<Any>()

    @SuppressLint("CheckResult")
    fun signUp(user: SignUpRequest){
        retrofitRepository.signUp(user)
            .subscribeOn(Schedulers.io())
            .delay(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
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
}
