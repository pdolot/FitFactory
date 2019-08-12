package com.example.fitfactory.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fitfactory.R
import com.example.fitfactory.app.App

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        (application as App).setCurrentActivity(this)
//        findNavController(R.id.nav_host_fragment).navigate(R.id.mapFragment)
    }

}
