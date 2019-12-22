package com.example.fitfactory.data.models.app

data class PassType(
    val id: Long,
    val fullName: String? = null,
    val shortName: String? = null,
    val durationInDays: Int? = null,
    val periodPrice: Double? = null,
    val description: String? = null,
    val benefits: List<String>? = null,
    val promoImage: String? = null

)