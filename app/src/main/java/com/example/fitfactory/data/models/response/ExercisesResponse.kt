package com.example.fitfactory.data.models.response

import com.example.fitfactory.data.models.app.Exercise
import com.google.gson.annotations.SerializedName

data class ExercisesResponse(
    @SerializedName("data")
    val data: List<Exercise>? = null
): BaseResponse()