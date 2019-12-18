package com.example.fitfactory.data.models.app

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CreditCard(
    @PrimaryKey(autoGenerate = false)
    var ownerId: Long = 0,
    var cardNumber: String = "",
    var expiryDate: String = "",
    var cvcCvv: String = ""
)