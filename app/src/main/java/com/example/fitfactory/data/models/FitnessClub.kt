package com.example.fitfactory.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FitnessClub(
    @PrimaryKey( autoGenerate = true)
    val id: Long? = null,
    val name: String? = null,

    @Embedded
    val address: Address? = null,
    val phoneNumber: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,

    @Embedded
    val openHours: OpenHours? = null,
    val menLockerRoomLimit: Int = 0,
    val womenLockerRoomLimit: Int = 0,
    val currentNumberOfMen: Int = 0,
    val currentNumberOfWomen: Int = 0
)

data class OpenHours(
    val monday: String? = null,
    val tuesday: String? = null,
    val wednesday: String? = null,
    val thursday: String? = null,
    val friday: String? = null,
    val saturday: String? = null,
    val sunday: String? = null
)
