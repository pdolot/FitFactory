package com.example.fitfactory.presentation.activities.splashActivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fitfactory.R
import com.example.fitfactory.presentation.activities.mainActivity.MainActivity

class SplashActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startMainActivity()

//        AccessToken.getCurrentAccessToken()?.let {
//            if (!it.isExpired) {
//                val profile = Profile.getCurrentProfile()
//                user.firstName = profile.firstName
//                user.lastName = profile.lastName
//                user.picture = profile.getProfilePictureUri(300, 300).toString()
//            }
//        }
//
//        GoogleSignIn.getLastSignedInAccount(applicationContext)?.let {
//            user.firstName = it.givenName ?: ""
//            user.lastName = it.familyName ?: ""
//            user.picture = it.photoUrl.toString()
//        }
    }

    private fun startMainActivity() {
        val mainActivity = Intent(this, MainActivity::class.java)
        mainActivity.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        mainActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        mainActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        mainActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(mainActivity)
    }
}