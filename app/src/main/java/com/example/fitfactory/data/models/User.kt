package com.example.fitfactory.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var firstName: String = "",
    var lastName: String = "",
    var picture: String = ""
)