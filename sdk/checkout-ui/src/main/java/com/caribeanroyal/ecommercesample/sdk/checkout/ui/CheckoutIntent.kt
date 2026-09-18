package com.caribeanroyal.ecommercesample.sdk.checkout.ui

import com.caribeanroyal.ecommercesample.sdk.checkout.core.PaymentProcessor

sealed class CheckoutIntent {
    object LoadProcessors : CheckoutIntent()
    data class SubmitPayment(val amount: Double, val currency: String, val processor: PaymentProcessor) : CheckoutIntent()
    
    // Booking Wizard Intents
    object NextStep : CheckoutIntent()
    object PreviousStep : CheckoutIntent()
    data class UpdatePartySize(val size: Int) : CheckoutIntent()
    data class SelectRoom(val type: String) : CheckoutIntent()
    data class ToggleDrinkPackage(val included: Boolean) : CheckoutIntent()
    data class ToggleWiFi(val included: Boolean) : CheckoutIntent()
    data class UpdateGuestInfo(val firstName: String, val lastName: String) : CheckoutIntent()
}
