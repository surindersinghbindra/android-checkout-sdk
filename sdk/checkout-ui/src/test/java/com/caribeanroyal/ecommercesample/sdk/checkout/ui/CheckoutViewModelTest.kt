package com.caribeanroyal.ecommercesample.sdk.checkout.ui

import app.cash.turbine.test
import com.caribeanroyal.ecommercesample.sdk.checkout.core.CheckoutSdk
import com.caribeanroyal.ecommercesample.sdk.checkout.core.PaymentProcessor
import com.caribeanroyal.ecommercesample.sdk.checkout.core.PaymentProcessorType
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CheckoutViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val checkoutSdk = mockk<CheckoutSdk>(relaxed = true)
    private lateinit var viewModel: CheckoutViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { checkoutSdk.enabledProcessors } returns emptyList()
        viewModel = CheckoutViewModel(checkoutSdk)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test SubmitPayment intent transitions to Success`() = runTest {
        coEvery { checkoutSdk.executeCheckout(100.0, "USD", PaymentProcessorType.STRIPE, any()) } returns true
        coEvery { checkoutSdk.submitPayment(100.0, "USD", PaymentProcessorType.STRIPE) } returns true
        val processor = mockk<PaymentProcessor>()
        every { processor.type } returns PaymentProcessorType.STRIPE

        viewModel.state.test {
            awaitItem() // Initial state

            viewModel.handleIntent(CheckoutIntent.SubmitPayment(100.0, "USD", processor))
            
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)
            val successState = awaitItem()
            assertEquals(false, successState.isLoading)
            assertEquals(true, successState.isSuccess)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
