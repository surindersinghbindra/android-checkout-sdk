package com.caribeanroyal.ecommercesample.sdk.checkout.core

/**
 * Strategy interface defining the contract for processing payments within the SDK.
 * Host applications must provide an implementation of this (e.g., Stripe, Adyen).
 */
interface PaymentProcessor {
    /**
     * Executes the payment transaction.
     * 
     * @param amount The amount to be processed.
     * @param currency The ISO currency code.
     * @return true if successful, false otherwise.
     */
    suspend fun processPayment(amount: Double, currency: String): Boolean
}
