package com.example.fitfactory.app

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.example.fitfactory.di.Injector
import com.facebook.FacebookSdk
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.stripe.android.PaymentConfiguration

class App : Application() {

    lateinit var activity: AppCompatActivity

    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(applicationContext, "pk_test_zdQZ0W00LyaDjOqv9DsSoWZJ000GPtvdRW")
    }

    fun setCurrentActivity(activity: AppCompatActivity) {
        this.activity = activity
    }

}