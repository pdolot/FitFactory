package com.example.fitfactory.presentation.pages.payment.fitnessLessonPayment

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.fitfactory.data.database.creditCard.CreditCardDao
import com.example.fitfactory.data.models.app.StateComplete
import com.example.fitfactory.data.models.app.StateError
import com.example.fitfactory.data.models.app.StateInProgress
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import com.example.fitfactory.functional.localStorage.LocalStorage
import com.example.fitfactory.presentation.base.BaseViewModel
import com.example.fitfactory.utils.SuperValidator
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_penalty_payment.*
import kotlinx.coroutines.*
import javax.inject.Inject

class FitnessLessonPaymentViewModel : BaseViewModel() {

    val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)
    private var validators = ArrayList<SuperValidator>()
    val validateResult = MutableLiveData<Boolean>()
    val callState = MutableLiveData<Any>()
    var isJobPaused = false


    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    @Inject
    lateinit var creditCardDao: CreditCardDao

    @Inject
    lateinit var localStorage: LocalStorage


    init {
        Injector.component.inject(this)
    }

    fun payForLesson(id: Long){
        callState.postValue(StateInProgress())
        rxDisposer.add(retrofitRepository.payForLesson(id)
            .subscribeBy(
                onSuccess = {
                    if (it.status){
                        callState.postValue(StateComplete())
                    }else{
                        callState.postValue(StateError(it.message))
                    }
                },
                onError = {
                    callState.postValue(StateError("Błąd połączenia z serwerem"))
                }
            ))
    }

    private fun startValidation(){
        scope.launch {
            while (isActive){
                if(!isJobPaused) {
                    validateResult.postValue(validate())
                }
                delay(1000)
            }
        }
    }

    fun validate(): Boolean{
        validators.forEach {
            if (!it.validate()){
                return false
            }
        }
        return true
    }

    fun addValidators(owner: Fragment) {
        validators.add(
            SuperValidator.Builder()
                .on(owner.cardNo)
                .canBeEmpty(false)
                .withExactLength(19)
                .build()
        )

        validators.add(
            SuperValidator.Builder()
                .on(owner.cardExpiry)
                .canBeEmpty(false)
                .withExactLength(5)
                .build()
        )

        validators.add(
            SuperValidator.Builder()
                .on(owner.cardCvc)
                .canBeEmpty(false)
                .withExactLength(3)
                .build()
        )

        startValidation()
    }
}