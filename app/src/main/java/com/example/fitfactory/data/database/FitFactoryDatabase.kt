package com.example.fitfactory.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fitfactory.data.models.User

@Database(
    entities = [User::class], version = 1
)
abstract class FitFactoryDatabase : RoomDatabase()