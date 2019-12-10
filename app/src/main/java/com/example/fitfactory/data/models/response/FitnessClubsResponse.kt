package com.example.fitfactory.data.models.response

import com.example.fitfactory.data.models.app.FitnessClub
import com.google.gson.annotations.SerializedName

data class FitnessClubsResponse(
    @SerializedName("data")
    val data: List<FitnessClub>? = null
): BaseResponse()