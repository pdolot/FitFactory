package com.example.fitfactory.presentation.pages.map

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.fitfactory.data.database.fitnessClub.FitnessClubRepository
import com.example.fitfactory.data.models.app.FitnessClub
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MapViewModel : BaseViewModel() {

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    @Inject
    lateinit var fitnessClubRepository: FitnessClubRepository

    init {
        Injector.component.inject(this)
    }

    @SuppressLint("CheckResult")
    fun fetchFitnessClubs() {
        rxDisposer.add(retrofitRepository.getAllFitnessClub()
            .repeatWhen { completed ->
                completed.delay(1, TimeUnit.MINUTES)
            }
            .subscribeBy(
                onNext = {
                    deleteAll()
                    if (it.status) {
                        insert(it.data)
                    }
                },
                onError = {
                    Log.e("MapFragment", it.message)
                }
            ))
    }

    private fun insert(fitnessClubs: List<FitnessClub>?) = viewModelScope.launch {
        fitnessClubRepository.insert(fitnessClubs)
    }

    private fun deleteAll() = viewModelScope.launch {
        fitnessClubRepository.deleteAll()
    }
}
