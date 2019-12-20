package com.example.fitfactory.data.models.response

import com.example.fitfactory.data.models.app.PassType
import com.google.gson.annotations.SerializedName

data class PassesTypeResponse(
    @SerializedName("data")
    val data: List<PassType>? = null
): BaseResponse()