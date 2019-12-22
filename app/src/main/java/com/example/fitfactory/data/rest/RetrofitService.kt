package com.example.fitfactory.data.rest

import com.example.fitfactory.data.models.app.UserGetResource
import com.example.fitfactory.data.models.request.*
import com.example.fitfactory.data.models.response.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitService{

    // User
    @POST("user/signUp")
    fun signUp(@Body signUpRequest: SignUpRequest): Single<BaseResponse>

    @POST("user/signIn")
    fun signIn(@Body signInRequest: SignInRequest): Single<AuthResponse>

    @POST("user/changePassword")
    fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Single<BaseResponse>

    @GET("user/getEntries")
    fun getUserEntries(@Query("id") userId: Long): Single<EntriesResponse>

    @GET("user/getLessons")
    fun getUserFitnessLesson(@Query("id") userId: Long): Single<LessonEntriesResponse>

    @GET("user/getPasses")
    fun getUserPasses(@Query("id") userId: Long): Single<PassesResponse>

    @POST("user/edit")
    fun editUser(@Body user: UserGetResource): Single<UserResponse>

    // PassType
    @GET("passType/getAll")
    fun getAllPassType(): Single<PassesTypeResponse>

    @GET("passType/getPass")
    fun getPassTypeById(@Query("id") passId: Long): Single<PassTypeResponse>

    // Pass
    @POST("pass/buy")
    fun buyPass(@Body buyPassRequest: BuyPassRequest): Single<BaseResponse>

    // FitnessClub
    @GET("fitnessClub/getFitnessClubs")
    fun getAllFitnessClub(): Observable<FitnessClubsResponse>

    // Exercises
    @GET("fitnessExercise/getAllExercises")
    fun getAllExercises(): Single<ExercisesResponse>

    // FitnessLesson
    @GET ("/fitnessLesson/getAll")
    fun getAllFitnessLesson(): Single<FitnessLessonResponse>

    @POST ("/lessonEntry/signUp")
    fun signUpToLesson(@Body fitnessLessonSigning: FitnessLessonSigning): Single<BaseResponse>

    @POST ("/lessonEntry/signOut")
    fun signOutFromLesson(@Body fitnessLessonSigning: FitnessLessonSigning): Single<BaseResponse>

}