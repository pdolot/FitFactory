package com.example.fitfactory.di.modules

import androidx.room.Room
import com.example.fitfactory.app.App
import com.example.fitfactory.data.database.FitFactoryDatabase
import com.example.fitfactory.data.database.fitnessClub.FitnessClubDao
import com.example.fitfactory.data.database.fitnessClub.FitnessClubRepository
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

    @Singleton
    @Provides
    fun provideFitnessClubDao(database: FitFactoryDatabase): FitnessClubDao{
        return database.fitnessClubDao()
    }

    @Singleton
    @Provides
    fun provideFitnessClubRepository(fitnessClubDao: FitnessClubDao): FitnessClubRepository{
        return FitnessClubRepository(fitnessClubDao)
    }
}