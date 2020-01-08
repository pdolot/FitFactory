package com.example.fitfactory.presentation.pages.payment.fitnessLessonPayment

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.fitfactory.constants.Payment
import com.example.fitfactory.data.database.creditCard.CreditCardDao
import com.example.fitfactory.data.models.app.StateComplete
import com.example.fitfactory.data.models.app.StateError
import com.example.fitfactory.data.models.app.StateInProgress
import com.example.fitfactory.data.models.request.PaymentForLesson
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import com.example.fitfactory.functional.localStorage.LocalStorage
import com.example.fitfactory.presentation.base.BaseViewModel
import com.example.fitfactory.utils.SuperValidator
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_fitness_lesson_payment.*

import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

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

    @Inject
    lateinit var context: Context


    init {
        Injector.component.inject(this)
    }

    fun payForLesson(id: Long, token: String, amount: Double){
        rxDisposer.add(retrofitRepository.payForLesson(PaymentForLesson(id, token, amount))
            .subscribeBy(
                onSuccess = {
                    if (it.status){
                        callState.postValue(StateComplete())
                    }else{
                        callState.postValue(StateError(it.message))
                    }
                },
                onError = {
                    Log.e("PAYMENT",it.message)
                    callState.postValue(StateError("Błąd połączenia z serwerem"))
                }
            ))
    }

    fun createToken(expiryDate: String, cvc: String, entryId: Long, amount: Double){
        callState.postValue(StateInProgress())
        val stripe = Stripe(context, PaymentConfiguration.getInstance(context).publishableKey)
        val expMonth = expiryDate.substring(0, 2).toInt()
        val expYear = expiryDate.substring(3, 5).toInt()
        val card = Card.create(Payment.CARD_NO, expMonth ,expYear, cvc)
        stripe.createCardToken(card, UUID.randomUUID().toString(), object :
            ApiResultCallback<Token> {
            override fun onError(e: Exception) {
                callState.postValue(StateError("Błąd płatności"))
                Log.e("PAYMENT",e.message)
            }

            override fun onSuccess(result: Token) {
                val token = result.id
                payForLesson(entryId, token, amount)
            }

        })
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