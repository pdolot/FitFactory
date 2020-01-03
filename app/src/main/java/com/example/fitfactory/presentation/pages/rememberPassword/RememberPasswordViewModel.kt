package com.example.fitfactory.presentation.pages.rememberPassword

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.fitfactory.constants.RegularExpression
import com.example.fitfactory.data.models.app.StateComplete
import com.example.fitfactory.data.models.app.StateError
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.base.BaseViewModel
import com.example.fitfactory.utils.SuperValidator
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_remember_password.*
import javax.inject.Inject

class RememberPasswordViewModel : BaseViewModel() {
    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    init {
        Injector.component.inject(this)
    }

    val callResult = MutableLiveData<Any>()

    private val validators = ArrayList<SuperValidator>()

    fun rememberPassword(email: String) {
        rxDisposer.add(
            retrofitRepository.rememberPassword(email)
                .subscribeBy(
                    onSuccess = {
                        if (it.status) {
                            callResult.postValue(StateComplete("Wysłano"))
                        } else {
                            callResult.postValue(StateError("Błędny email"))
                        }
                    },
                    onError = {
                        callResult.postValue(StateError("Błąd połączenia"))
                    }
                )
        )
    }

    fun validate(): Boolean{
        validators.forEach {
            if (!it.validate()){
                return false
            }
        }
        return true
    }

    fun addValidators(owner: Fragment){
        validators.add(SuperValidator.Builder()
            .on(owner.rememberPasswordFragment_userEmail)
            .canBeEmpty(false)
            .withRegex(RegularExpression.EMAIL.toRegex())
            .build())
    }
}
