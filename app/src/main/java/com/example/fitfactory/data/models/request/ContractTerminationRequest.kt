package com.example.fitfactory.data.models.request

data class ContractTerminationRequest(
    var id: Long,
    var token: String,
    var amount: Double
)