package com.example.fitfactory.data.models.app

import androidx.room.Entity
import androidx.room.PrimaryKey

data class SharedPrefUser(
    var username: String,
    var password: String,
    var email: String
)