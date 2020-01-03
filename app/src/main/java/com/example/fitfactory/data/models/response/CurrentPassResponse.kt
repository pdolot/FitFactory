package com.example.fitfactory.data.models.response

import com.google.gson.annotations.SerializedName

data class CurrentPassResponse(
    @SerializedName("data")
    val data: String?
) : BaseResponse()