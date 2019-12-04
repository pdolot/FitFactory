package com.example.fitfactory.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fitfactory.data.database.fitnessClub.FitnessClubDao
import com.example.fitfactory.data.models.app.FitnessClub

@Database(
    entities = [FitnessClub::class], version = 1
)
abstract class FitFactoryDatabase : RoomDatabase(){

    abstract fun fitnessClubDao(): FitnessClubDao
}