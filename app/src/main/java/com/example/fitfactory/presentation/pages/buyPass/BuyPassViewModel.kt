package com.example.fitfactory.presentation.pages.buyPass

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfactory.data.models.app.PassType
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import com.example.fitfactory.functional.localStorage.LocalStorage
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class BuyPassViewModel : ViewModel() {

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    @Inject
    lateinit var localStorage: LocalStorage

    init {
        Injector.component.inject(this)
    }

    var passes = MutableLiveData<List<PassType>>()

    @SuppressLint("CheckResult")
    fun getPassesType(){
        println(localStorage.readToken())
        retrofitRepository.getAllPassType()
            .subscribeBy (
                onSuccess = {
                    if (it.status){
                        passes.postValue(it.data)
                    }
                },
                onError = {
                    println(it.message)
                }
            )
    }

}
