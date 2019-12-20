package com.example.fitfactory.presentation.pages.fitnessLesson

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.fitfactory.data.database.fitnessClub.FitnessClubRepository
import com.example.fitfactory.data.models.app.*
import com.example.fitfactory.data.models.app.FitnessLesson
import com.example.fitfactory.data.models.request.FitnessLessonSigning
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

    @Inject
    lateinit var fitnessClubRepository: FitnessClubRepository

    init {
        Injector.component.inject(this)
    }

    val callResult = MutableLiveData<List<FitnessLesson>>()

    var fitnessClubs: List<Pair<Long, String>>? = null

    var signUpResult = MutableLiveData<Any>()

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

    fun fetchFitnessClub(owner: LifecycleOwner){
        fitnessClubRepository.getAll().observe(owner, Observer {
            fitnessClubs = it.map { fitnessClub ->
                Pair(fitnessClub.id!!, fitnessClub.name!!) }
        })
    }

    fun signUpToLesson(lessonId: Long){
        signUpResult.postValue(StateInProgress())
        val userId = localStorage.getUser()?.id
        userId?.let {
            rxDisposer.add(retrofitRepository.signUpToLesson(FitnessLessonSigning(userId, lessonId))
                .subscribeBy(
                    onSuccess = {
                        if (it.status){
                            signUpResult.postValue(StateComplete(it.message))
                        }else{
                            signUpResult.postValue(StateError(it.message))
                        }

                    },
                    onError = {
                        signUpResult.postValue(StateError("Błąd połączenia z serwerem"))
                    }
                ))
        }
    }

}
