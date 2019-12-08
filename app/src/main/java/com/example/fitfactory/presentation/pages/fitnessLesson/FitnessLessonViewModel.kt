package com.example.fitfactory.presentation.pages.fitnessLesson

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.fitfactory.data.models.app.FitnessLesson
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import com.example.fitfactory.functional.localStorage.LocalStorage
import com.example.fitfactory.presentation.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class FitnessLessonViewModel : BaseViewModel() {

    @Inject
    lateinit var localStorage: LocalStorage

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    init {
        Injector.component.inject(this)
    }

    val callResult = MutableLiveData<List<FitnessLesson>>()

    fun fetchAllFitnessLesson(){
        rxDisposer.add(retrofitRepository.getAllFitnessLesson()
            .subscribeBy(
                onSuccess = {
                    if (it.status){
                        callResult.postValue(it.data)
                    }
                },
                onError = {
                    Log.e("FitnessLessonViewModel", it.message)
                }
            )
        )
    }

}
