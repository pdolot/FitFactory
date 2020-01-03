package com.example.fitfactory.data.rest

import com.example.fitfactory.data.models.app.UserGetResource
import com.example.fitfactory.data.models.request.*
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

    // User

    fun signUp(signUpRequest: SignUpRequest): Single<BaseResponse> {
        return retrofitService.signUp(signUpRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }

    fun rememberPassword(email: String): Single<BaseResponse> {
        return retrofitService.rememberPassword(email)
            .delaySubscription(2500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }

    fun signIn(signInRequest: SignInRequest): Single<AuthResponse> {
        return retrofitService.signIn(signInRequest).subscribeOn(Schedulers.io())
            .delay(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun changePassword(changePasswordRequest: ChangePasswordRequest): Single<BaseResponse> {
        return retrofitService.changePassword(changePasswordRequest).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getActivePass(userId: Long): Single<CurrentPassResponse> {
        return retrofitService.getActivePass(userId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun editUser(user: UserGetResource) : Single<UserResponse>{
        return retrofitService.editUser(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun getUserEntries(userId: Long): Single<EntriesResponse> {
        return retrofitService.getUserEntries(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUserFitnessLesson(userId: Long): Single<LessonEntriesResponse> {
        return retrofitService.getUserFitnessLesson(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUserPasses(userId: Long): Single<PassesResponse> {
        return retrofitService.getUserPasses(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    // PassType

    fun getAllPassType(): Single<PassesTypeResponse> {
        return retrofitService.getAllPassType().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getPassTypeById(passId: Long): Single<PassTypeResponse>{
        return retrofitService.getPassTypeById(passId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    // Pass

    fun buyPass(buyPassRequest: BuyPassRequest): Single<BaseResponse>{
        return retrofitService.buyPass(buyPassRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun measurePenalty(passId: Long): Single<PenaltyResponse>{
        return retrofitService.measurePenalty(passId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun contractTermination(passId: Long): Single<BaseResponse>{
        return retrofitService.contractTermination(passId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    // FitnessClub

    fun getAllFitnessClub(): Observable<FitnessClubsResponse> {
        return retrofitService.getAllFitnessClub().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    // Exercises

    fun getAllExercises(): Single<ExercisesResponse> {
        return retrofitService.getAllExercises().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    // FitnessLesson
    fun getAllFitnessLesson(): Single<FitnessLessonResponse> {
        return retrofitService.getAllFitnessLesson().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun signUpToLesson(fitnessLessonSigning: FitnessLessonSigning): Single<BaseResponse>{
        return retrofitService.signUpToLesson(fitnessLessonSigning).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun signOutFromLesson(fitnessLessonSigning: FitnessLessonSigning): Single<BaseResponse>{
        return retrofitService.signOutFromLesson(fitnessLessonSigning).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun payForLesson(entryId: Long): Single<BaseResponse>{
        return retrofitService.payForLesson(entryId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}