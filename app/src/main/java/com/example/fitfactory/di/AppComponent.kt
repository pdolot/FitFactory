package com.example.fitfactory.di

import com.example.fitfactory.di.modules.AppModule
import com.example.fitfactory.di.modules.DbModule
import com.example.fitfactory.di.modules.RestModule
import com.example.fitfactory.di.modules.UserModule
import com.example.fitfactory.presentation.activities.LoginActivity
import com.example.fitfactory.presentation.activities.mainActivity.MainActivity
import com.example.fitfactory.presentation.activities.mainActivity.MainViewModel
import com.example.fitfactory.presentation.fragments.mapFragment.MapFragment
import com.example.fitfactory.presentation.fragments.mapFragment.MapViewModel
import com.example.fitfactory.presentation.fragments.navigationFragment.NavigationFragment
import com.example.fitfactory.presentation.fragments.navigationFragment.NavigationRecyclerViewAdapter
import com.example.fitfactory.presentation.fragments.rememberPasswordFragment.RememberPasswordFragment
import com.example.fitfactory.presentation.fragments.rememberPasswordFragment.RememberPasswordViewModel
import com.example.fitfactory.presentation.fragments.signInFragment.SignInFragment
import com.example.fitfactory.presentation.fragments.signInFragment.SignInViewModel
import com.example.fitfactory.presentation.fragments.signUpFragment.SignUpFragment
import com.example.fitfactory.presentation.fragments.signUpFragment.SignUpViewModel
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
    fun inject(into: NavigationFragment)

    // ViewModels
    fun inject(into: SignInViewModel)
    fun inject(into: SignUpViewModel)
    fun inject(into: RememberPasswordViewModel)
    fun inject(into: MapViewModel)

    // Utils
    fun inject(into: BitmapHelper)

    // Adapters
    fun inject(into: NavigationRecyclerViewAdapter)


}