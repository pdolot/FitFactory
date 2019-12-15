package com.example.fitfactory.data.models.app

data class UserGetResource(

    val id: Long? = null,

    val username: String? = null,

    val email: String? = null,

    val firstName: String? = null,

    val secondName: String? = null,

    val lastName: String? = null,

    val identityNumber: String? = null,

    val phoneNumber: String? = null,

    val address: Address? = null,

    val birthDate: String? = null,

    val profileImage: String? = null,

    val entriesCount: Int = 0
)
