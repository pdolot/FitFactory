package com.example.fitfactory.data.models.request

data class PaymentForLesson(
    var id: Long,
    var token: String,
    var amount: Double
)