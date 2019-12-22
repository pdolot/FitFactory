package com.example.fitfactory.presentation.pages.payment

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.fitfactory.data.models.app.*
import com.example.fitfactory.data.models.request.BuyPassRequest
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import com.example.fitfactory.functional.localStorage.LocalStorage
import com.example.fitfactory.presentation.base.BaseViewModel
import com.example.fitfactory.utils.SuperValidator
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.coroutines.*
import org.joda.time.DateTime
import javax.inject.Inject

class PaymentViewModel : BaseViewModel() {

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    @Inject
    lateinit var localStorage: LocalStorage

    init {
        Injector.component.inject(this)
    }

    var passId: Long? = null
        set(value) {
            field = value
            fetchPassTypeById()
        }

    val passType = MutableLiveData<PassType?>()
    val callState = MutableLiveData<Any>()

    var date: DateTime = DateTime.now()

    private var validators = ArrayList<SuperValidator>()
    val validateResult = MutableLiveData<Boolean>()

    val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private fun fetchPassTypeById() {
        rxDisposer.add(retrofitRepository.getPassTypeById(passId ?: return)
            .subscribeBy(
                onSuccess = {
                    if (it.status) {
                        passType.postValue(it.data)
                    }
                },
                onError = {
                    Log.e("Payment", it.message)
                }
            ))
    }

    fun buyPass(user: UserGetResource, date: String) {
        callState.postValue(StateInProgress())
        val buyPassRequest =
            BuyPassRequest(passId ?: return, date, user, localStorage.getUser()?.id ?: return)
        rxDisposer.add(
            retrofitRepository.buyPass(buyPassRequest).subscribeBy(
                onSuccess = {
                    if (it.status) {
                        callState.postValue(StateComplete())
                    } else {
                        callState.postValue(StateError(it.message))
                    }
                },
                onError = {
                    callState.postValue(StateError("Błąd połączenia z serwerem"))
                }
            )
        )
    }

    private fun startValidation(){
        scope.launch {
            while (isActive){
                validateResult.postValue(validate())
                delay(1000)
            }
        }
    }

    private fun validate(): Boolean{
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

        validators.add(
            SuperValidator.Builder()
                .on(owner.firstName)
                .canBeEmpty(false)
                .build()
        )


        validators.add(
            SuperValidator.Builder()
                .on(owner.lastName)
                .canBeEmpty(false)
                .build()
        )


        validators.add(
            SuperValidator.Builder()
                .on(owner.userPersonalIdentity)
                .canBeEmpty(false)
                .minLength(9)
                .build()
        )


        validators.add(
            SuperValidator.Builder()
                .on(owner.userBirthDate)
                .canBeEmpty(false)
                .withExactLength(10)
                .build()
        )


        validators.add(
            SuperValidator.Builder()
                .on(owner.userAddressStreet)
                .canBeEmpty(false)
                .minLength(6)
                .build()
        )

        validators.add(
            SuperValidator.Builder()
                .on(owner.userAddressCity)
                .canBeEmpty(false)
                .minLength(2)
                .build()
        )


        validators.add(
            SuperValidator.Builder()
                .on(owner.userZipCode)
                .canBeEmpty(false)
                .withExactLength(6)
                .build()
        )


        validators.add(
            SuperValidator.Builder()
                .on(owner.userZipCodeCity)
                .canBeEmpty(false)
                .minLength(2)
                .build()
        )

        startValidation()
    }
}