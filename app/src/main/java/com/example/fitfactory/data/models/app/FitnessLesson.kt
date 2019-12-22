package com.example.fitfactory.data.models.app

data class FitnessLesson(

    val id: Long? = null,

    val name: String? = null,

    val coach: UserGetResource? = null,

    val fitnessClub: FitnessClub? = null,

    val peopleLimit: Int? = null,

    val date: String? = null,

    var signedUpPeopleCount: Int? = null,

    val price: Double? = null,

    val description: String? = null,

    val isActive: Boolean? = null,

    val promoImage: String? = null
)

