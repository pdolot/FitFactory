package com.example.fitfactory.presentation.fragments.signUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfactory.data.models.request.SignUpRequest
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
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

    var response = MutableLiveData<Any>()

    fun signUpUser(username: String, password: String, email: String) {
        retrofitRepository.signUp(SignUpRequest(username, email, password))
            .subscribeOn(Schedulers.io())
            .delay(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    response?.postValue(it)
                },
                onError = {
                    println(it.message)
                    response?.postValue(it)
                }
            )
    }
}
