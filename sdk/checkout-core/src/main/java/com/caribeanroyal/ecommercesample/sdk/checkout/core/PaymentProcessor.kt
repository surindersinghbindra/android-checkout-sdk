package com.caribeanroyal.ecommercesample.sdk.checkout.core

import androidx.annotation.Keep

/**
 * Enum defining the built-in payment processors supported by the SDK.
 */
@Keep
enum class PaymentProcessorType(val displayName: String) {
    ADYEN("Adyen"),
    STRIPE("Stripe"),
    HEADLESS("Custom Headless")
}

/**
 * Strategy interface defining the contract for processing payments within the SDK.
 */
@Keep
interface PaymentProcessor {
    val type: PaymentProcessorType
    
    /**
     * Executes the payment transaction.
     * 
     * @param amount The amount to be processed.
     * @param currency The ISO currency code.
     * @return true if successful, false otherwise.
     */
    suspend fun processPayment(amount: Double, currency: String): Boolean
}
