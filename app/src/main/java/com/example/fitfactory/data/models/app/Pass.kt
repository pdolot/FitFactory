package com.example.fitfactory.data.models.app

data class Pass(
    val passType: PassType? = null,
    val boughtDate: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val isActive: Boolean? = null,
    var qrCode: String? = "https://pl.qr-code-generator.com/wp-content/themes/qr/new_structure/markets/core_market/generator/dist/generator/assets/images/websiteQRCode_noFrame.png"
)