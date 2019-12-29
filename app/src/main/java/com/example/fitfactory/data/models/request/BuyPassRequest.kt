package com.example.fitfactory.data.models.request

import com.example.fitfactory.data.models.app.PassUser

data class BuyPassRequest(
    var passTypeId: Long,
    var startDate: String,
    var passUser: PassUser,
    var ownerId: Long
)