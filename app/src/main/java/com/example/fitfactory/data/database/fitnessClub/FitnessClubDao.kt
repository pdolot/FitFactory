package com.example.fitfactory.data.database.fitnessClub

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fitfactory.data.models.app.FitnessClub

@Dao
interface FitnessClubDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fitnessClubs: List<FitnessClub>?)

    @Query("SELECT * from FitnessClub")
    fun getAll(): LiveData<List<FitnessClub>>

    @Query("DELETE FROM FitnessClub")
    suspend fun deleteAll()
}