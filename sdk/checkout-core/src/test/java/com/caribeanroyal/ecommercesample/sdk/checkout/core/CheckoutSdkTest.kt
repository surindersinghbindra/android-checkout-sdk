package com.caribeanroyal.ecommercesample.sdk.checkout.core

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import android.content.Context
import io.mockk.every
import io.mockk.mockkStatic
import androidx.startup.AppInitializer
import com.caribeanroyal.ecommercesample.sdk.checkout.core.db.CheckoutDatabase
import com.caribeanroyal.ecommercesample.sdk.checkout.core.db.IdempotencyDao
import com.caribeanroyal.ecommercesample.sdk.checkout.core.db.CheckoutSdkInitializer

class CheckoutSdkTest {

    // [v1.0 API Compatibility Test - DO NOT MODIFY]
    @Test
    fun testLegacyInit_v1_0() = runBlocking {
                mockkStatic(AppInitializer::class)
        val mockInitializer = mockk<AppInitializer>(relaxed = true)
        every { AppInitializer.getInstance(any()) } returns mockInitializer
        val mockDb = mockk<CheckoutDatabase>(relaxed = true)
        val mockDao = mockk<IdempotencyDao>(relaxed = true)
        every { mockDb.idempotencyDao() } returns mockDao
        every { mockInitializer.initializeComponent(CheckoutSdkInitializer::class.java) } returns mockDb

        val mockProcessor = mockk<PaymentProcessor>()
        coEvery { mockProcessor.processPayment(10.0, "USD") } returns true

        val context = mockk<Context>(relaxed = true)
        val sdk = CheckoutSdk.Builder(context).setPaymentProcessor(mockProcessor).build()
        
        val result = sdk.executeCheckout(10.0, "USD")
        assertTrue(result)
        assertEquals("production", sdk.environment)
    }

    // [v1.1 API Compatibility Test - DO NOT MODIFY]
    @Test
    fun testBuilderInit_v1_1() = runBlocking {
                mockkStatic(AppInitializer::class)
        val mockInitializer = mockk<AppInitializer>(relaxed = true)
        every { AppInitializer.getInstance(any()) } returns mockInitializer
        val mockDb = mockk<CheckoutDatabase>(relaxed = true)
        val mockDao = mockk<IdempotencyDao>(relaxed = true)
        every { mockDb.idempotencyDao() } returns mockDao
        every { mockInitializer.initializeComponent(CheckoutSdkInitializer::class.java) } returns mockDb

        val mockProcessor = mockk<PaymentProcessor>()
        coEvery { mockProcessor.processPayment(20.0, "EUR") } returns true

        val context = mockk<Context>(relaxed = true)
        val sdk = CheckoutSdk.Builder(context)
            .setPaymentProcessor(mockProcessor)
            .setEnvironment("staging")
            .build()
            
        val result = sdk.executeCheckout(20.0, "EUR")
        assertTrue(result)
        assertEquals("staging", sdk.environment)
    }
}
