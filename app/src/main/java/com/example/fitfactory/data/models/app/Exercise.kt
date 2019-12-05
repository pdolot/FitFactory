package com.example.fitfactory.data.models.app

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise(
    @PrimaryKey( autoGenerate = false)
    val id: Long,
    val name: String,
    val startPosition: List<String>,
    val movement: List<String>,
    val tips: List<String>,
    val images: List<String>,
    val bodyParts: List<String>,
    val qrCode: String
)