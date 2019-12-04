package com.example.fitfactory.data.models.response

import com.example.fitfactory.data.models.UserGetResource

data class AuthResponse(
    val token: String,
    val user: UserGetResource
)