package com.example.fitfactory.data.database.exercise

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fitfactory.data.models.app.Exercise
import io.reactivex.Single

@Dao
interface ExerciseDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercises: List<Exercise>?)

    @Query("SELECT * from Exercise")
    fun getAll(): LiveData<List<Exercise>>

    @Query("SELECT * FROM Exercise WHERE qrCode =:qrCode")
    fun getExercisesByQrCode(qrCode: String): Single<List<Exercise>>

    @Query("DELETE FROM Exercise")
    suspend fun deleteAll()
}