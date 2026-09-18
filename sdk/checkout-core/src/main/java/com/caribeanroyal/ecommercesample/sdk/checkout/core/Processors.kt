package com.caribeanroyal.ecommercesample.sdk.checkout.core

import kotlinx.coroutines.delay

class AdyenProcessor : PaymentProcessor {
    override val type = PaymentProcessorType.ADYEN

    override suspend fun processPayment(amount: Double, currency: String, idempotencyKey: String?): Boolean {
        println("Processing $amount $currency via internal ADYEN processor")
        delay(1500)
        return true
    }
}

class StripeProcessor : PaymentProcessor {
    override val type = PaymentProcessorType.STRIPE

    override suspend fun processPayment(amount: Double, currency: String, idempotencyKey: String?): Boolean {
        println("Processing $amount $currency via internal STRIPE processor")
        delay(1500)
        return true
    }
}

class FailProcessor : PaymentProcessor {
    override val type = PaymentProcessorType.FAIL_SIMULATOR

    override suspend fun processPayment(amount: Double, currency: String, idempotencyKey: String?): Boolean {
        println("Processing $amount $currency via internal FAIL simulator")
        delay(1000)
        return false // Simulate failure
    }
}
