package com.example.fitfactory.presentation.pages.yoursActivity

import androidx.lifecycle.MutableLiveData
import com.example.fitfactory.data.models.app.EmptyData
import com.example.fitfactory.data.models.request.FitnessLessonSigning
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import com.example.fitfactory.functional.localStorage.LocalStorage
import com.example.fitfactory.presentation.base.BaseViewModel
import com.example.fitfactory.utils.TimeUtil
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class YourActivityViewModel : BaseViewModel() {

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    @Inject
    lateinit var localStorage: LocalStorage

    var fitnessLessonItem: FitnessLessonItem = FitnessLessonItem()
    var passesItem: PassesItem = PassesItem()

    init {
        Injector.component.inject(this)
    }


    fun fetchLessonEntries() {
        rxDisposer.add(retrofitRepository.getUserFitnessLesson(localStorage.getUser()?.id ?: return)
            .subscribeBy(
                onSuccess = {
                    fitnessLessonItem.lessonEntries.postValue(it.data ?: listOf(EmptyData()))
                },
                onError = {
                    fitnessLessonItem.lessonEntries.postValue(listOf(EmptyData()))
                }
            ))
    }

    fun fetchUserPasses() {
        rxDisposer.add(retrofitRepository.getUserPasses(localStorage.getUser()?.id ?: return)
            .subscribeBy(
                onSuccess = {
                    it.data?.let {
                        it.forEach { pass->
                            pass.isActive = TimeUtil.isDateBetween(pass.startDate, pass.endDate, dateFormat = "dd/MM/yyyy")
                        }
                    }
                    passesItem.passes.postValue(it.data ?: listOf(EmptyData()))
                },
                onError = {
                    passesItem.passes.postValue(listOf(EmptyData()))
                }
            ))
    }

    fun signOutFromLesson(id: Long){
        rxDisposer.add(retrofitRepository.signOutFromLesson(
            FitnessLessonSigning(localStorage.getUser()?.id ?: return, id)
        ).subscribeBy(
            onSuccess = {
                if (it.status){
                    fetchLessonEntries()
                }
            },
            onError = {
                println(it.message)
            }
        ))
    }


}

class PassesItem {
    var passes = MutableLiveData<List<Any>>()
}

class FitnessLessonItem {
    var lessonEntries = MutableLiveData<List<Any>>()
}