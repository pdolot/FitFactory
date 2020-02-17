package com.example.fitfactory.di

import com.example.fitfactory.app.App
import com.example.fitfactory.di.modules.AppModule
import com.example.fitfactory.di.modules.DbModule
import com.example.fitfactory.di.modules.RestModule
import com.example.fitfactory.di.modules.UserModule

object Injector {
    lateinit var component: AppComponent

    fun init(application: App, baseUrl: String) {
        component = DaggerAppComponent.builder()
            .dbModule(DbModule(application))
            .restModule(RestModule(baseUrl))
            .appModule(AppModule(application))
            .userModule(UserModule(application))
            .build()
    }

}