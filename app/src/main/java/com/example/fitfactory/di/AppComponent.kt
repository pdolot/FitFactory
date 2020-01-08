package com.example.fitfactory.di

import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.data.rest.TokenInterceptor
import com.example.fitfactory.di.modules.AppModule
import com.example.fitfactory.di.modules.DbModule
import com.example.fitfactory.di.modules.RestModule
import com.example.fitfactory.di.modules.UserModule
import com.example.fitfactory.presentation.activities.mainActivity.MainActivity
import com.example.fitfactory.presentation.activities.splashActivity.SplashActivity
import com.example.fitfactory.presentation.customViews.TopBar
import com.example.fitfactory.presentation.customViews.flexibleLayout.FlexibleView
import com.example.fitfactory.presentation.pages.buyPass.PassToBuyAdapter
import com.example.fitfactory.presentation.pages.map.MapFragment
import com.example.fitfactory.presentation.pages.map.MapViewModel
import com.example.fitfactory.presentation.pages.yoursActivity.passes.PassAdapter
import com.example.fitfactory.presentation.pages.rememberPassword.RememberPasswordFragment
import com.example.fitfactory.presentation.pages.rememberPassword.RememberPasswordViewModel
import com.example.fitfactory.presentation.pages.signIn.SignInFragment
import com.example.fitfactory.presentation.pages.signIn.SignInViewModel
import com.example.fitfactory.presentation.pages.signUp.SignUpFragment
import com.example.fitfactory.presentation.pages.signUp.SignUpViewModel
import com.example.fitfactory.presentation.navigationDrawer.NavigationDrawer
import com.example.fitfactory.presentation.navigationDrawer.NavigationRecyclerViewAdapter
import com.example.fitfactory.presentation.pages.buyPass.BuyPassViewModel
import com.example.fitfactory.presentation.pages.entriesHistory.EntriesHistoryViewModel
import com.example.fitfactory.presentation.pages.exercises.exercise.ExerciseViewModel
import com.example.fitfactory.presentation.pages.exercises.scannerView.CameraViewViewModel
import com.example.fitfactory.presentation.pages.fitnessLesson.FitnessLesson
import com.example.fitfactory.presentation.pages.fitnessLesson.FitnessLessonAdapter
import com.example.fitfactory.presentation.pages.fitnessLesson.FitnessLessonViewModel
import com.example.fitfactory.presentation.pages.payment.contractTerminationPayment.ContractTerminationViewModel
import com.example.fitfactory.presentation.pages.payment.fitnessLessonPayment.FitnessLessonPaymentViewModel
import com.example.fitfactory.presentation.pages.payment.passPayment.PaymentViewModel
import com.example.fitfactory.presentation.pages.settings.editProfile.EditProfile
import com.example.fitfactory.presentation.pages.settings.editProfile.EditProfileViewModel
import com.example.fitfactory.presentation.pages.yoursActivity.YourActivityViewModel
import com.example.fitfactory.presentation.pages.yoursActivity.fitnessLesson.FitnessLessonSimpleAdapter
import com.example.fitfactory.utils.BitmapHelper
import com.example.fitfactory.utils.Validator
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        DbModule::class,
        RestModule::class,
        UserModule::class
    ]
)
interface AppComponent {
    // Activities
    fun inject(into: MainActivity)

    // Fragments
    fun inject(into: SignInFragment)
    fun inject(into: SignUpFragment)
    fun inject(into: RememberPasswordFragment)
    fun inject(into: MapFragment)
    fun inject(into: NavigationDrawer)

    // ViewModels
    fun inject(into: SignInViewModel)
    fun inject(into: SignUpViewModel)
    fun inject(into: RememberPasswordViewModel)
    fun inject(into: MapViewModel)
    fun inject(into: BuyPassViewModel)
    fun inject(into: ExerciseViewModel)

    // Utils
    fun inject(into: BitmapHelper)
    fun inject(into: TokenInterceptor)
    fun inject(into: RetrofitRepository)
    fun inject(into: Validator)

    // Adapters
    fun inject(into: NavigationRecyclerViewAdapter)
    fun inject(into: PassToBuyAdapter)
    fun inject(into: PassAdapter)

    // Views
    fun inject(into: FlexibleView)
    fun inject(into: TopBar)
    fun inject(into: FitnessLessonViewModel)
    fun inject(into: FitnessLessonAdapter)
    fun inject(into: FitnessLesson)
    fun inject(into: EntriesHistoryViewModel)
    fun inject(into: EditProfileViewModel)
    fun inject(into: EditProfile)
    fun inject(into: YourActivityViewModel)
    fun inject(into: FitnessLessonSimpleAdapter)
    fun inject(into: PaymentViewModel)
    fun inject(into: SplashActivity)
    fun inject(into: ContractTerminationViewModel)
    fun inject(into: FitnessLessonPaymentViewModel)
    fun inject(into: CameraViewViewModel)

}