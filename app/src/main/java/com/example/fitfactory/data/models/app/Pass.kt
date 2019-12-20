package com.example.fitfactory.data.models.app

data class Pass(
    val id: Long = 0,
    val passType: PassType? = null,
    val boughtDate: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val passUser: UserGetResource? = null,
    var qrCode: String? = null
){
    var isActive: Boolean = false
}