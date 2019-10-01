package com.example.fitfactory.data.models

enum class PassType(
    val passName: String, val durationInDays: Int,
    val price: Double, val description: String,
    val benefits: List<String>) {

    PROAGE_12M("ProAge12M", 365, 89.99,
        "Oferta przeznaczona dla studentów, uczniów do 26 roku życia z ważną legitymacją",
        listOf("Dostęp do sprzętu siłowni", "Sauna", "Jeden trening personalny w miesiącu", "Możliwość pomiaru ciała")),
    ACCESS_1M("ProAge12M", 31, 89.99,
        "Oferta przeznaczona dla studentów, uczniów do 26 roku życia z ważną legitymacją",
        listOf("Dostęp do sprzętu siłowni", "Sauna", "Jeden trening personalny w miesiącu", "Możliwość pomiaru ciała")),
    PROAGE_6M("ProAge12M", 120, 89.99,
        "Oferta przeznaczona dla studentów, uczniów do 26 roku życia z ważną legitymacją",
        listOf("Dostęp do sprzętu siłowni", "Sauna", "Jeden trening personalny w miesiącu", "Możliwość pomiaru ciała"))
}