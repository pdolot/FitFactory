package com.example.fitfactory.presentation.fragments.signUp

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfactory.data.models.request.SignUpRequest
import com.example.fitfactory.data.models.response.BaseResponse
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SignUpViewModel : ViewModel() {
    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    init {
        Injector.component.inject(this)
    }

    var response : MutableLiveData<Any>? = null

    fun signUpUser(username: String, email: String, password: String) {
        retrofitRepository.signUp(SignUpRequest(username, email, password))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseResponse>{
                override fun onComplete() {
                    println("Complete ")
                }

                override fun onSubscribe(d: Disposable) {
                    println("Start ")
                }

                override fun onNext(t: BaseResponse) {
                    println("ODPOWIEDŹ")
                    response?.postValue(t)
                }

                override fun onError(e: Throwable) {
                    response?.postValue(e)
                }

            })
//            .subscribeBy(
//                onSuccess = {
//                    response?.postValue(it)
//                },
//                onError = {
//                    response?.postValue(it)
//                }
//            )
    }
}
