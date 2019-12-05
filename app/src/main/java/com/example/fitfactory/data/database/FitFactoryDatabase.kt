package com.example.fitfactory.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fitfactory.data.database.exercise.ExerciseDao
import com.example.fitfactory.data.database.fitnessClub.FitnessClubDao
import com.example.fitfactory.data.models.app.Exercise
import com.example.fitfactory.data.models.app.FitnessClub

@Database(
    entities = [FitnessClub::class, Exercise::class], version = 1
)
@TypeConverters( Converters::class)
abstract class FitFactoryDatabase : RoomDatabase(){

    abstract fun fitnessClubDao(): FitnessClubDao
    abstract fun exercisesDao(): ExerciseDao
}