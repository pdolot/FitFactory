package com.example.fitfactory.di

import com.example.fitfactory.di.modules.AppModule
import com.example.fitfactory.di.modules.DbModule
import com.example.fitfactory.di.modules.RestModule
import com.example.fitfactory.presentation.activities.LoginActivity
import com.example.fitfactory.presentation.fragments.mainFragment.MainFragment
import com.example.fitfactory.presentation.fragments.mainFragment.MainViewModel
import com.example.fitfactory.presentation.fragments.signInFragment.SignInFragment
import com.example.fitfactory.presentation.fragments.signInFragment.SignInViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        DbModule::class,
        RestModule::class
    ]
)
interface AppComponent {
    fun inject(into: LoginActivity)

    // Fragments
    fun inject(into: MainFragment)

    fun inject(into: SignInFragment)

    // ViewModels
    fun inject(into: MainViewModel)

    fun inject(into: SignInViewModel)
}