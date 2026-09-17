package com.caribeanroyal.ecommercesample.sdk.checkout.ui

/**
 * Intents representing user actions in the Checkout SDK.
 */
sealed class CheckoutIntent {
    data class SubmitPayment(val amount: Double, val currency: String) : CheckoutIntent()
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
    // E.g., a newly added property in v1.1, defaulted for backward compatibility
    val showDiscountField: Boolean = false 
)
