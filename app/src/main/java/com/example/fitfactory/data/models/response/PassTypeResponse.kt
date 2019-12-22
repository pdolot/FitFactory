package com.example.fitfactory.data.models.response

import com.example.fitfactory.data.models.app.PassType
import com.google.gson.annotations.SerializedName

data class PassTypeResponse(
    @SerializedName("data")
    val data: PassType? = null
) : BaseResponse()