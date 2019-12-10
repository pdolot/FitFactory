package com.example.fitfactory.data.rest

import com.example.fitfactory.data.models.request.SignInRequest
import com.example.fitfactory.data.models.request.SignUpRequest
import com.example.fitfactory.data.models.response.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitService{
    @POST("user/signUp")
    fun signUp(@Body signUpRequest: SignUpRequest): Single<BaseResponse>

    @POST("user/signIn")
    fun signIn(@Body signInRequest: SignInRequest): Single<AuthResponse>


    // PassType
    @GET("passType/getAll")
    fun getAllPassType(): Single<PassesResponse>

    // FitnessClub
    @GET("fitnessClub/getFitnessClubs")
    fun getAllFitnessClub(): Observable<FitnessClubsResponse>

    // Exercises
    @GET("fitnessExercise/getAllExercises")
    fun getAllExercises(): Single<ExercisesResponse>
}