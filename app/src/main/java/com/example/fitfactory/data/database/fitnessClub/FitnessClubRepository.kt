package com.example.fitfactory.data.database.fitnessClub

import androidx.lifecycle.LiveData
import com.example.fitfactory.data.models.FitnessClub

class FitnessClubRepository (private val fitnessClubDao: FitnessClubDao){

    suspend fun insert(fitnessClubs: List<FitnessClub>?) = fitnessClubDao.insert(fitnessClubs)

    fun getAll(): LiveData<List<FitnessClub>> = fitnessClubDao.getAll()
}