package com.example.fitfactory.di.modules

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.fitfactory.app.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: App) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideSupportFragmentManager(): FragmentManager {
        return application.activity.supportFragmentManager
    }

    @Provides
    @Singleton
    fun provideActivity(): AppCompatActivity {
        return application.activity
    }
}