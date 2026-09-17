package com.caribeanroyal.ecommercesample.sdk.checkout.core

/**
 * Headless entry point for the Checkout SDK business logic.
 */
class CheckoutSdk private constructor(
    val paymentProcessor: PaymentProcessor,
    val environment: String
) {

    /**
     * Legacy initialization method from v1.0 of the SDK.
     * @deprecated Use the newer Builder or primary initializer.
     */
    @Deprecated(
        message = "This initialization method is obsolete. Use the new builder which supports environment configuration.",
        replaceWith = ReplaceWith("CheckoutSdk.Builder().setPaymentProcessor(paymentProcessor).build()")
    )
    constructor(paymentProcessor: PaymentProcessor) : this(paymentProcessor, "production")

    class Builder {
        private var paymentProcessor: PaymentProcessor? = null
        private var environment: String = "production"

        fun setPaymentProcessor(processor: PaymentProcessor) = apply { this.paymentProcessor = processor }
        
        @JvmOverloads
        fun setEnvironment(env: String = "production") = apply { this.environment = env }

        fun build(): CheckoutSdk {
            val processor = paymentProcessor ?: throw IllegalStateException("PaymentProcessor must be provided.")
            return CheckoutSdk(processor, environment)
        }
    }

    /**
     * Executes the checkout flow.
     */
    suspend fun executeCheckout(amount: Double, currency: String): Boolean {
        // Here we could add telemetry, logging, and other core headless logic before calling the strategy.
        return paymentProcessor.processPayment(amount, currency)
    }
}
