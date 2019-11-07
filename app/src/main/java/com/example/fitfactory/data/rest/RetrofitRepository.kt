package com.example.fitfactory.data.rest

import com.example.fitfactory.data.models.request.SignUpRequest
import com.example.fitfactory.data.models.response.BaseResponse
import com.example.fitfactory.di.Injector
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class RetrofitRepository @Inject constructor(private val retrofitService: RetrofitService) {

    init {
        Injector.component.inject(this)
    }


    fun signUp(signUpRequest: SignUpRequest): Observable<BaseResponse>{
        return retrofitService.signUp(signUpRequest)
    }

}