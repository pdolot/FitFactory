package com.example.fitfactory.data.models.request

import com.example.fitfactory.data.models.app.UserGetResource

data class BuyPassRequest(
    var passTypeId: Long,
    var startDate: String,
    var passUser: UserGetResource,
    var ownerId: Long
)