package com.example.fitfactory.data.models.request

data class ChangePasswordRequest(
    var username: String,
    var oldPassword: String,
    var newPassword: String
)