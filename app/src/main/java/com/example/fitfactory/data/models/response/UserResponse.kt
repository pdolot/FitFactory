package com.example.fitfactory.data.models.response

import com.example.fitfactory.data.models.app.UserGetResource
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("data")
    val data: UserGetResource
): BaseResponse()