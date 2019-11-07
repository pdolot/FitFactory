package com.example.fitfactory.data.rest

import com.example.fitfactory.data.models.request.SignUpRequest
import com.example.fitfactory.data.models.response.BaseResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService{
    @POST("http://192.168.1.105:8080/user/signUp")
    fun signUp(@Body signUpRequest: SignUpRequest): Observable<BaseResponse>
}