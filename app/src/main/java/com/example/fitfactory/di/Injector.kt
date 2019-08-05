package com.example.fitfactory.di

import com.example.fitfactory.app.App
import com.example.fitfactory.di.modules.AppModule

object Injector {
    lateinit var component: AppComponent
    fun init(application: App) {
        component = DaggerAppComponent.builder()
            .appModule(AppModule(application))
            .build()
    }
}