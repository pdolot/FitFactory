package com.example.fitfactory.di

import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.data.rest.TokenInterceptor
import com.example.fitfactory.di.modules.AppModule
import com.example.fitfactory.di.modules.DbModule
import com.example.fitfactory.di.modules.RestModule
import com.example.fitfactory.di.modules.UserModule
import com.example.fitfactory.presentation.activities.mainActivity.MainActivity
import com.example.fitfactory.presentation.activities.mainActivity.MainViewModel
import com.example.fitfactory.presentation.customViews.TopBar
import com.example.fitfactory.presentation.customViews.flexibleLayout.FlexibleView
import com.example.fitfactory.presentation.pages.buyPass.PassToBuyAdapter
import com.example.fitfactory.presentation.pages.map.MapFragment
import com.example.fitfactory.presentation.pages.map.MapViewModel
import com.example.fitfactory.presentation.pages.pass.PassAdapter
import com.example.fitfactory.presentation.pages.rememberPassword.RememberPasswordFragment
import com.example.fitfactory.presentation.pages.rememberPassword.RememberPasswordViewModel
import com.example.fitfactory.presentation.pages.signIn.SignInFragment
import com.example.fitfactory.presentation.pages.signIn.SignInViewModel
import com.example.fitfactory.presentation.pages.signUp.SignUpFragment
import com.example.fitfactory.presentation.pages.signUp.SignUpViewModel
import com.example.fitfactory.presentation.navigationDrawer.NavigationDrawer
import com.example.fitfactory.presentation.navigationDrawer.NavigationRecyclerViewAdapter
import com.example.fitfactory.presentation.pages.buyPass.BuyPassViewModel
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
    fun inject(into: MainViewModel)

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

    // Utils
    fun inject(into: BitmapHelper)

    // Adapters
    fun inject(into: NavigationRecyclerViewAdapter)
    fun inject(into: PassToBuyAdapter)
    fun inject(into: PassAdapter)

    // Views
    fun inject(into: FlexibleView)

    fun inject(into: TopBar)
    fun inject(into: TokenInterceptor)
    fun inject(into: RetrofitRepository)
    fun inject(into: Validator)
    fun inject(into: BuyPassViewModel)


}