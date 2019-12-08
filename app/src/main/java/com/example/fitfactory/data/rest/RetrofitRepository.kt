package com.example.fitfactory.data.rest

import com.example.fitfactory.data.models.request.SignInRequest
import com.example.fitfactory.data.models.request.SignUpRequest
import com.example.fitfactory.data.models.response.*
import com.example.fitfactory.di.Injector
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RetrofitRepository @Inject constructor(private val retrofitService: RetrofitService) {

    init {
        Injector.component.inject(this)
    }


    fun signUp(signUpRequest: SignUpRequest): Single<BaseResponse>{
        return retrofitService.signUp(signUpRequest)
            .subscribeOn(Schedulers.io())
            .delay(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun signIn(signInRequest: SignInRequest) : Single<AuthResponse>{
        return retrofitService.signIn(signInRequest).subscribeOn(Schedulers.io())
            .delay(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
    }

    // PassType

    fun getAllPassType(): Single<PassesResponse> {
        return  retrofitService.getAllPassType().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    // FitnessClub

    fun getAllFitnessClub(): Observable<FitnessClubsResponse>{
        return  retrofitService.getAllFitnessClub().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    // Exercises

    fun getAllExercises(): Single<ExercisesResponse>{
        return  retrofitService.getAllExercises().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    // FitnessLesson
    fun getAllFitnessLesson(): Single<FitnessLessonResponse>{
        return  retrofitService.getAllFitnessLesson().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}