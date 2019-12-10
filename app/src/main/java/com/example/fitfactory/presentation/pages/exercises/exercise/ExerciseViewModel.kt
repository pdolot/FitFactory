package com.example.fitfactory.presentation.pages.exercises.exercise

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfactory.data.database.exercise.ExerciseRepository
import com.example.fitfactory.data.models.app.Exercise
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class ExerciseViewModel : BaseViewModel() {

    @Inject
    lateinit var exerciseRepository: ExerciseRepository

    val exercises = MutableLiveData<List<Exercise>>()
    var qrCode: String? = null
        set(value) {
            field = value
            fetchExercisesFromDb(value ?: return)
        }
    init {
        Injector.component.inject(this)
    }

    private fun fetchExercisesFromDb(qrCode: String){
        rxDisposer.add(exerciseRepository.getExercisesByQrCode(qrCode)
            .subscribeBy(
                onSuccess = {
                    exercises.postValue(it)
                },
                onError = {
                    Log.e("ExerciseViewModel", it.message)
                }
            ))
    }
}