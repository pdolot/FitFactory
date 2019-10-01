package com.example.fitfactory.di

import com.example.fitfactory.di.modules.AppModule
import com.example.fitfactory.di.modules.DbModule
import com.example.fitfactory.di.modules.RestModule
import com.example.fitfactory.di.modules.UserModule
import com.example.fitfactory.presentation.activities.LoginActivity
import com.example.fitfactory.presentation.activities.mainActivity.MainActivity
import com.example.fitfactory.presentation.activities.mainActivity.MainViewModel
import com.example.fitfactory.presentation.fragments.buyPass.PassToBuyAdapter
import com.example.fitfactory.presentation.fragments.map.MapFragment
import com.example.fitfactory.presentation.fragments.map.MapViewModel
import com.example.fitfactory.presentation.fragments.pass.PassAdapter
import com.example.fitfactory.presentation.fragments.rememberPassword.RememberPasswordFragment
import com.example.fitfactory.presentation.fragments.rememberPassword.RememberPasswordViewModel
import com.example.fitfactory.presentation.fragments.signIn.SignInFragment
import com.example.fitfactory.presentation.fragments.signIn.SignInViewModel
import com.example.fitfactory.presentation.fragments.signUp.SignUpFragment
import com.example.fitfactory.presentation.fragments.signUp.SignUpViewModel
import com.example.fitfactory.presentation.navigationDrawer.NavigationDrawer
import com.example.fitfactory.presentation.navigationDrawer.NavigationRecyclerViewAdapter
import com.example.fitfactory.utils.BitmapHelper
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
    fun inject(into: LoginActivity)
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


}