package com.example.fitfactory.presentation.pages.buyPass

import androidx.lifecycle.MutableLiveData
import com.example.fitfactory.data.models.app.PassType
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import com.example.fitfactory.functional.localStorage.LocalStorage
import com.example.fitfactory.presentation.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class BuyPassViewModel : BaseViewModel() {

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    @Inject
    lateinit var localStorage: LocalStorage

    init {
        Injector.component.inject(this)
    }

    var passes = MutableLiveData<List<PassType>>()

    fun getPassesType(){
        rxDisposer.add(retrofitRepository.getAllPassType()
            .subscribeBy (
                onSuccess = {
                    if (it.status){
                        passes.postValue(it.data)
                    }else{
                        passes.postValue(emptyList())
                    }
                },
                onError = {
                    println(it.message)
                }
            ))
    }

}
