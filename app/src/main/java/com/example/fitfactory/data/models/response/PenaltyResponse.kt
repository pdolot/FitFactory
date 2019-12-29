package com.example.fitfactory.data.models.response

import com.google.gson.annotations.SerializedName

data class PenaltyResponse(
    @SerializedName("data")
    val data: Double? = null
) : BaseResponse()