package com.caribeanroyal.ecommercesample.sdk.checkout.core

/**
 * Headless entry point for the Checkout SDK business logic.
 */
class CheckoutSdk private constructor(
    val enabledProcessors: List<PaymentProcessor>,
    val environment: String
) {

    class Builder {
        private val enabledProcessors = mutableListOf<PaymentProcessor>()
        private var environment: String = "production"

        fun enableProcessors(types: List<PaymentProcessorType>) = apply { 
            types.forEach { type ->
                when(type) {
                    PaymentProcessorType.ADYEN -> enabledProcessors.add(AdyenProcessor())
                    PaymentProcessorType.STRIPE -> enabledProcessors.add(StripeProcessor())
                    PaymentProcessorType.HEADLESS -> { /* handled by host app if needed, omitting here for simplicity */ }
                }
            }
        }
        
        @JvmOverloads
        fun setEnvironment(env: String = "production") = apply { this.environment = env }

        fun build(): CheckoutSdk {
            if (enabledProcessors.isEmpty()) {
                throw IllegalStateException("At least one PaymentProcessorType must be enabled.")
            }
            return CheckoutSdk(enabledProcessors, environment)
        }
    }

    /**
     * Executes the checkout flow using the specified processor.
     */
    suspend fun executeCheckout(amount: Double, currency: String, processorType: PaymentProcessorType): Boolean {
        val processor = enabledProcessors.find { it.type == processorType } 
            ?: throw IllegalArgumentException("Processor $processorType is not enabled in the SDK.")
        return processor.processPayment(amount, currency)
    }
}
