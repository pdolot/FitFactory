package com.example.fitfactory.data.models

data class Exercise(
    var images: List<String>? = null,
    var name: String? = null,
    var tips: String? = null,
    var movement: String? = null,
    var startPosition: String? = null,
    var bodyParts: List<BodyPart>? = null
)