package com.example.fitfactory.presentation.pages.map

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
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

    var fitnessClubs = MutableLiveData<List<FitnessClub>>()

    private var oldFitnessClubs: List<FitnessClub> = emptyList()

    @SuppressLint("CheckResult")
    fun fetchFitnessClubs() {
        rxDisposer.add(retrofitRepository.getAllFitnessClub()
            .repeatWhen { completed -> completed.delay(1, TimeUnit.MINUTES) }
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

//    fun getFitnessClub(): List<FitnessClub>{
//        return listOf(
//            FitnessClub(
//                name = "FitFactory 1",
//                address = Address(
//                    "Jana Matejki 3/5",
//                    "Łódź",
//                    "91-402",
//                    "Łódź"
//                ),
//                latitude = 51.782529,
//                longitude = 19.484287,
//                openHour = "06:00",
//                closeHour = "23:00",
//                phoneNumber = "453034234",
//                peopleLimit = 200,
//                peopleCountAtThisTime = 60
//            ),
//            FitnessClub(
//                name = "FitFactory 2",
//                address = Address(
//                    "Zgierska 211B",
//                    "Łódź",
//                    "91-497",
//                    "Łódź"
//                ),
//                latitude = 51.818834,
//                longitude = 19.435536,
//                openHour = "06:00",
//                closeHour = "23:00",
//                phoneNumber = "345204485",
//                peopleLimit = 200,
//                peopleCountAtThisTime = 20
//            ),
//            FitnessClub(
//                name = "FitFactory 3",
//                address = Address(
//                    "Aleksandrowska 38",
//                    "Łódź",
//                    "91-151",
//                    "Łódź"
//                ),
//                latitude = 51.798456,
//                longitude = 19.392620,
//                openHour = "06:00",
//                closeHour = "23:00",
//                phoneNumber = "743824931",
//                peopleLimit = 200,
//                peopleCountAtThisTime = 80
//            ),
//            FitnessClub(
//                name = "FitFactory 4",
//                address = Address(
//                    "Jana Kilińskiego 122-128",
//                    "Łódź",
//                    "90-316",
//                    "Łódź"
//                ),
//                latitude = 51.761922,
//                longitude = 19.469181,
//                openHour = "06:00",
//                closeHour = "23:00",
//                phoneNumber = "875349234",
//                peopleLimit = 200,
//                peopleCountAtThisTime = 120
//            ),
//            FitnessClub(
//                name = "FitFactory 5",
//                address = Address(
//                    "Generała Jarosława Dąbrowskiego 207/225",
//                    "Łódź",
//                    "93-231",
//                    "Łódź"
//                ),
//                latitude = 51.735671,
//                longitude = 19.517075,
//                openHour = "06:00",
//                closeHour = "23:00",
//                phoneNumber = "875349234",
//                peopleLimit = 200,
//                peopleCountAtThisTime = 180
//            ),
//            FitnessClub(
//                name = "FitFactory 6",
//                address = Address(
//                    "aleja Politechniki 10",
//                    "Łódź",
//                    "90-001",
//                    "Łódź"
//                ),
//                latitude = 51.748426,
//                longitude = 19.450814,
//                openHour = "06:00",
//                closeHour = "23:00",
//                phoneNumber = "875349234",
//                peopleLimit = 200,
//                peopleCountAtThisTime = 200
//            )
//        )
//    }
}
