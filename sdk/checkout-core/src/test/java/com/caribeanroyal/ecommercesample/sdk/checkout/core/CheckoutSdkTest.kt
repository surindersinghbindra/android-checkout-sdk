package com.caribeanroyal.ecommercesample.sdk.checkout.core

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CheckoutSdkTest {

    // [v1.0 API Compatibility Test - DO NOT MODIFY]
    @Test
    fun testLegacyInit_v1_0() = runBlocking {
        val mockProcessor = mockk<PaymentProcessor>()
        coEvery { mockProcessor.processPayment(10.0, "USD") } returns true

        @Suppress("DEPRECATION")
        val sdk = CheckoutSdk(mockProcessor)
        
        val result = sdk.executeCheckout(10.0, "USD")
        assertTrue(result)
        assertEquals("production", sdk.environment)
    }

    // [v1.1 API Compatibility Test - DO NOT MODIFY]
    @Test
    fun testBuilderInit_v1_1() = runBlocking {
        val mockProcessor = mockk<PaymentProcessor>()
        coEvery { mockProcessor.processPayment(20.0, "EUR") } returns true

        val sdk = CheckoutSdk.Builder()
            .setPaymentProcessor(mockProcessor)
            .setEnvironment("staging")
            .build()
            
        val result = sdk.executeCheckout(20.0, "EUR")
        assertTrue(result)
        assertEquals("staging", sdk.environment)
    }
}
