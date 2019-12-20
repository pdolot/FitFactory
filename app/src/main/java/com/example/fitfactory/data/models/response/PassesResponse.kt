package com.example.fitfactory.data.models.response

import com.example.fitfactory.data.models.app.Pass
import com.google.gson.annotations.SerializedName

data class PassesResponse(
    @SerializedName("data")
    val data: List<Pass>? = null
) : BaseResponse()