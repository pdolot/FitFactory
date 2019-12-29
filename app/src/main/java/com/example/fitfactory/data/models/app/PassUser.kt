package com.example.fitfactory.data.models.app

data class PassUser(
    var email: String,
    var firstName: String,
    var lastName: String,
    var identityNumber: String,
    var phoneNumber: String,
    var birthDate: String,
    var address: Address,
    var creditCard: CreditCard
)