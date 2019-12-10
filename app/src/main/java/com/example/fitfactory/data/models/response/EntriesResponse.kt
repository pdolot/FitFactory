package com.example.fitfactory.data.models.response

import com.example.fitfactory.data.models.app.Entry
import com.example.fitfactory.data.models.app.PassType
import com.google.gson.annotations.SerializedName

data class EntriesResponse(
    @SerializedName("data")
    val data: List<Entry>? = null
): BaseResponse()