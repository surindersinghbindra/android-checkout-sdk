package com.caribeanroyal.ecommercesample.sdk.checkout.core

import androidx.annotation.Keep

/**
 * Headless entry point for the Checkout SDK business logic.
 */
@Keep
class CheckoutSdk private constructor(
    val enabledProcessors: List<PaymentProcessor>,
    val environment: String,
    val debuggable: Boolean,
    val analytics: CheckoutAnalytics?
) {

    @Deprecated("Use Builder and enableProcessors instead")
    constructor(paymentProcessor: PaymentProcessor) : this(listOf(paymentProcessor), "production", false, null)

    @Keep
    class Builder {
        private val enabledProcessors = mutableListOf<PaymentProcessor>()
        private var environment: String = "production"
        private var debuggable: Boolean = false
        private var analytics: CheckoutAnalytics? = null

        fun enableProcessors(types: List<PaymentProcessorType>) = apply { 
            types.forEach { type ->
                when(type) {
                    PaymentProcessorType.ADYEN -> enabledProcessors.add(AdyenProcessor())
                    PaymentProcessorType.STRIPE -> enabledProcessors.add(StripeProcessor())
                    PaymentProcessorType.HEADLESS -> { /* handled by host app if needed, omitting here for simplicity */ }
                }
            }
        }
        
        @Deprecated("Use enableProcessors instead")
        fun setPaymentProcessor(processor: PaymentProcessor) = apply {
            enabledProcessors.add(processor)
        }
        
        @JvmOverloads
        fun setEnvironment(env: String = "production") = apply { this.environment = env }

        fun setDebuggable(debuggable: Boolean) = apply { this.debuggable = debuggable }

        fun setAnalytics(analytics: CheckoutAnalytics) = apply { this.analytics = analytics }

        fun build(): CheckoutSdk {
            if (enabledProcessors.isEmpty()) {
                throw IllegalStateException("At least one PaymentProcessorType must be enabled.")
            }
            return CheckoutSdk(enabledProcessors, environment, debuggable, analytics)
        }
    }

    @Deprecated(
        message = "This method will be replaced in the future to standardise naming conventions.",
        replaceWith = ReplaceWith("executeCheckout(amount, currency, processorType)")
    )
    suspend fun submitPayment(amount: Double, currency: String, processorType: PaymentProcessorType): Boolean {
        return executeCheckout(amount, currency, processorType)
    }

    /**
     * Executes the checkout flow using the specified processor.
     */
    suspend fun executeCheckout(amount: Double, currency: String, processorType: PaymentProcessorType): Boolean {
        val processor = enabledProcessors.find { it.type == processorType } 
            ?: throw IllegalArgumentException("Processor $processorType is not enabled in the SDK.")
        return processor.processPayment(amount, currency)
    }
    
    @Deprecated("Use executeCheckout with PaymentProcessorType instead")
    suspend fun executeCheckout(amount: Double, currency: String): Boolean {
        if (enabledProcessors.isEmpty()) return false
        return enabledProcessors.first().processPayment(amount, currency)
    }
    fun logEvent(tag: String, message: String) {
        if (debuggable) {
            println("[$tag] $message")
        }
    }

    fun logError(tag: String, message: String, throwable: Throwable? = null) {
        if (debuggable) {
            println("[$tag] ERROR: $message")
            throwable?.printStackTrace()
        }
        val t = throwable ?: Exception(message)
        analytics?.logError(t, "[$tag] $message")
    }
}