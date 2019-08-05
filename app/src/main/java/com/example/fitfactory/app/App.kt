package com.example.fitfactory.app

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.example.fitfactory.di.Injector

class App : Application() {

    lateinit var activity: AppCompatActivity

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
    }

    fun setCurrentActivity(activity: AppCompatActivity) {
        this.activity = activity
    }

}