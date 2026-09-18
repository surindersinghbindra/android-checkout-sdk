package com.caribeanroyal.ecommercesample.sdk.checkout.ui

import com.caribeanroyal.ecommercesample.sdk.checkout.core.PaymentProcessor

data class CheckoutState(
    val currentStep: Int = 0,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val availableProcessors: List<PaymentProcessor> = emptyList(),
    
    // Booking Wizard Data
    val partySize: Int = 2,
    val roomType: String = "Interior",
    val roomPrices: Map<String, Double> = mapOf(
        "Interior" to 0.0,
        "Outside" to 150.0,
        "Balcony" to 300.0,
        "Suite" to 800.0
    ),
    val includeDrinkPackage: Boolean = false,
    val includeWiFi: Boolean = false,
    val guestFirstName: String = "",
    val guestLastName: String = ""
)
