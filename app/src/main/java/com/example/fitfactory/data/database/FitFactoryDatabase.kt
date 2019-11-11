package com.example.fitfactory.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fitfactory.data.models.Address
import com.example.fitfactory.data.models.FitnessClub

@Database(
    entities = [Address::class], version = 1
)
abstract class FitFactoryDatabase : RoomDatabase()