package com.example.fitfactory.data.models

import androidx.room.Entity

@Entity
data class FitnessClub(
    val id: Long? = null,
    val name: String? = null,
    val address: Address? = null,
    val phoneNumber: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val openHour: String? = null,
    val closeHour: String? = null,
    val peopleLimit: Int = 0,
    val peopleCountAtThisTime: Int = 0
)
