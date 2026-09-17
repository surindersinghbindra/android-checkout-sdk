package com.caribeanroyal.ecommercesample.sdk.checkout.ui

import app.cash.turbine.test
import com.caribeanroyal.ecommercesample.sdk.checkout.core.CheckoutSdk
import io.mockk.coEvery
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
    private val checkoutSdk = mockk<CheckoutSdk>()
    private lateinit var viewModel: CheckoutViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CheckoutViewModel(checkoutSdk)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // [v1.0 API Compatibility Test - DO NOT MODIFY]
    @Test
    fun `test SubmitPayment intent transitions to Success`() = runTest {
        coEvery { checkoutSdk.executeCheckout(100.0, "USD") } returns true

        viewModel.state.test {
            assertEquals(CheckoutState(), awaitItem()) // Initial state

            viewModel.handleIntent(CheckoutIntent.SubmitPayment(100.0, "USD"))
            
            assertEquals(CheckoutState(isLoading = true), awaitItem())
            assertEquals(CheckoutState(isLoading = false, isSuccess = true), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
