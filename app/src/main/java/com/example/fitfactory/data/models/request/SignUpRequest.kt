package com.example.fitfactory.data.models.request

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    var username: String,

    var password: String,

    var email: String,

    var firstName: String? = null,

    var lastName: String? = null,

    var birthDate: String? = null,

    var profileImage: String? = null

)
