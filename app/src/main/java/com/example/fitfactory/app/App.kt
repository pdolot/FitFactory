package com.example.fitfactory.app

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.example.fitfactory.di.Injector
import com.facebook.FacebookSdk
import com.stripe.android.PaymentConfiguration

class App : Application() {

    lateinit var activity: AppCompatActivity

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
        PaymentConfiguration.init(applicationContext, "pk_test_zdQZ0W00LyaDjOqv9DsSoWZJ000GPtvdRW")
    }

    fun setCurrentActivity(activity: AppCompatActivity) {
        this.activity = activity
    }

}