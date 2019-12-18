package com.example.fitfactory.data.models.app

data class UserGetResource(

    var id: Long? = null,

    var username: String? = null,

    var email: String? = null,

    var firstName: String? = null,

    var secondName: String? = null,

    var lastName: String? = null,

    var identityNumber: String? = null,

    var phoneNumber: String? = null,

    var address: Address? = null,

    var birthDate: String? = null,

    var profileImage: String? = null,

    var entriesCount: Int = 0
)
