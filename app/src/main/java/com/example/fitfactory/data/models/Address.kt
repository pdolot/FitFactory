package com.example.fitfactory.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Address(
    @PrimaryKey
    val street: String,
    val city: String,
    val zipCode: String,
    val zipCodeCity: String
)