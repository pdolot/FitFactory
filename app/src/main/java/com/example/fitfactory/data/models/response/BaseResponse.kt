package com.example.fitfactory.data.models.response

import com.google.gson.annotations.SerializedName

open class BaseResponse(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
)
