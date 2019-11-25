package com.example.fitfactory.data.models.response

import com.example.fitfactory.data.models.PassType
import com.google.gson.annotations.SerializedName

data class PassesResponse(
    @SerializedName("data")
    val data: List<PassType>? = null
): BaseResponse()