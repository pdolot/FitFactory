package com.example.fitfactory.data.models.app

data class Exercise(
    var images: List<String>? = null,
    var name: String? = null,
    var tips: String? = null,
    var movement: String? = null,
    var startPosition: String? = null,
    var bodyParts: List<BodyPart>? = null
)