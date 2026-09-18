package com.caribeanroyal.ecommercesample.sdk.checkout.ui

import com.caribeanroyal.ecommercesample.sdk.checkout.core.PaymentProcessorType

/**
 * Intents representing user actions in the Checkout SDK.
 */
sealed class CheckoutIntent {
    data class SubmitPayment(
        val amount: Double,
        val currency: String,
        val processorType: PaymentProcessorType
    ) : CheckoutIntent()
    
    object RetryPayment : CheckoutIntent()
}

/**
 * Immutable State representing the UI state.
 * Rule: NEVER remove properties from this state. Provide defaults for new properties.
 */
data class CheckoutState @JvmOverloads constructor(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val showDiscountField: Boolean = false,
    val availableProcessors: List<PaymentProcessorType> = emptyList()
)
