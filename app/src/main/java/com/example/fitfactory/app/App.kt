package com.example.fitfactory.app

import android.app.Application
import androidx.appcompat.app.AppCompatActivity

class App : Application() {

    lateinit var activity: AppCompatActivity

    override fun onCreate() {
        super.onCreate()
    }

    fun setCurrentActivity(activity: AppCompatActivity) {
        this.activity = activity
    }

}