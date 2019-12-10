package com.example.fitfactory.presentation.pages.entriesHistory

import androidx.lifecycle.MutableLiveData
import com.example.fitfactory.data.models.app.Entry
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import com.example.fitfactory.functional.localStorage.LocalStorage
import com.example.fitfactory.presentation.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class EntriesHistoryViewModel : BaseViewModel() {

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    @Inject
    lateinit var localStorage: LocalStorage

    init {
        Injector.component.inject(this)
    }

    val entriesList = MutableLiveData<List<Entry>>()

    fun fetchEntries() {
        rxDisposer.add(
            retrofitRepository.getUserEntries(localStorage.getUser()?.id ?: return)
                .subscribeBy(
                    onSuccess = {
                        if (it.status) {
                            entriesList.postValue(it.data)
                        }
                    },
                    onError = {
                        //TODO
                    }
                )
        )

    }
}
