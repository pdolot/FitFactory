package com.example.fitfactory.data.rest

import com.example.fitfactory.data.models.request.SignUpRequest
import com.example.fitfactory.data.models.response.BaseResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService{
    @POST("/signUp")
    fun signUp(@Body signUpRequest: SignUpRequest): Single<BaseResponse>
}