package com.example.fitfactory.data.models.app

data class LessonEntry(
    val id: Long = 0,
    val fitnessLesson: FitnessLesson?,
    val isPaid: Boolean,
    val wasPresent: Boolean
)