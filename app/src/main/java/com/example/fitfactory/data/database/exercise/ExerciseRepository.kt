package com.example.fitfactory.data.database.exercise

import com.example.fitfactory.data.models.app.Exercise
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    suspend fun insert(exercises: List<Exercise>?) = exerciseDao.insert(exercises)

    fun getAll() = exerciseDao.getAll()

    fun getExercisesByQrCode(qrCode: String) =
        exerciseDao.getExercisesByQrCode(qrCode)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

    suspend fun deleteAll() = exerciseDao.deleteAll()
}