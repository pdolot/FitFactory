package com.example.fitfactory.data.models.response

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
)
