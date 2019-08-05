package com.example.fitfactory.di

import com.example.fitfactory.di.modules.AppModule
import com.example.fitfactory.presentation.activities.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class
    ]
)
interface AppComponent {
    fun inject(into: MainActivity)
}