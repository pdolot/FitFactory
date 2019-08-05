package com.example.fitfactory.di

import com.example.fitfactory.app.App
import com.example.fitfactory.di.modules.AppModule
import com.example.fitfactory.di.modules.DbModule
import com.example.fitfactory.di.modules.RestModule

object Injector {
    lateinit var component: AppComponent

    fun init(application: App) {
        component = DaggerAppComponent.builder()
            .dbModule(DbModule(application))
            .restModule(RestModule())
            .appModule(AppModule(application))
            .build()
    }
}