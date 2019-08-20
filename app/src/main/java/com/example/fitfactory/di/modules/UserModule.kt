package com.example.fitfactory.di.modules

import android.app.Application
import com.example.fitfactory.data.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserModule(val application: Application) {

    @Singleton
    @Provides
    fun provideUser() = User()

    @Singleton
    @Provides
    fun provideGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(application.applicationContext, gso)
    }
}