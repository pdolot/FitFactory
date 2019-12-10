package com.example.fitfactory.presentation.activities.splashActivity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fitfactory.data.database.exercise.ExerciseRepository
import com.example.fitfactory.data.models.app.Exercise
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel : BaseViewModel() {

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    @Inject
    lateinit var exerciseRepository: ExerciseRepository

    val callResult = MutableLiveData<Any>()

    init {
        Injector.component.inject(this)
        rxDisposer.add(retrofitRepository.getAllExercises()
            .subscribeBy(
                onSuccess = {
                    if (it.status){
                        insertToDb(it.data)
                        callResult.postValue(true)
                    }else{
                        callResult.postValue(false)
                    }

                },
                onError = {
                    Log.e("SplashViewModel", it.message)
                    callResult.postValue(false)
                }
            ))
    }

    private fun insertToDb(data: List<Exercise>?) = viewModelScope.launch {
        exerciseRepository.insert(data)
    }

}