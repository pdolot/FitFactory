package com.example.fitfactory.di.modules

import androidx.room.Room
import com.example.fitfactory.app.App
import com.example.fitfactory.data.database.FitFactoryDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule(private val application: App) {
    @Singleton
    @Provides
    fun provideRoomDatabase(): FitFactoryDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            FitFactoryDatabase::class.java,
            "FitFactoryDatabase"
        ).build()
    }
}