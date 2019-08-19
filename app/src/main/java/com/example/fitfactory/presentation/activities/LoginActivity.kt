package com.example.fitfactory.presentation.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fitfactory.R
import com.example.fitfactory.app.App
import com.example.fitfactory.data.models.User
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.activities.mainActivity.MainActivity
import com.facebook.AccessToken
import com.facebook.Profile
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        (application as App).setCurrentActivity(this)
        Injector.component.inject(this)
        val accessToken = AccessToken.getCurrentAccessToken()
        accessToken?.let {
            if (!it.isExpired) {
                val profile = Profile.getCurrentProfile()
                user.firstName = profile.firstName
                user.lastName = profile.lastName
                user.picture = profile.getProfilePictureUri(300, 300).toString()
                moveToMapFragment()
            }
        }

    }

    private fun moveToMapFragment() {
        val mainActivity = Intent(this, MainActivity::class.java)
        mainActivity.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(mainActivity)
        finish()
    }

}
