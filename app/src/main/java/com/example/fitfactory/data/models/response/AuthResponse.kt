package com.example.fitfactory.data.models.response

import com.example.fitfactory.data.models.app.UserGetResource

data class AuthResponse(
    val token: String,
    val user: UserGetResource
)