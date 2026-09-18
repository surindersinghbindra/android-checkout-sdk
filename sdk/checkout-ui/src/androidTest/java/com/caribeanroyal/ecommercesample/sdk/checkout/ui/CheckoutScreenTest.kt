package com.caribeanroyal.ecommercesample.sdk.checkout.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.caribeanroyal.ecommercesample.sdk.checkout.core.CheckoutSdk
import com.caribeanroyal.ecommercesample.sdk.checkout.core.PaymentProcessor
import com.caribeanroyal.ecommercesample.sdk.checkout.core.PaymentProcessorType
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CheckoutScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCheckoutScreenDisplaysOrderTitleAndTotal() {
        // Arrange
        val checkoutSdk = mockk<CheckoutSdk>(relaxed = true)
        val mockProcessor = mockk<PaymentProcessor>()
        every { mockProcessor.type } returns PaymentProcessorType.STRIPE
        
        every { checkoutSdk.enabledProcessors } returns listOf(mockProcessor)
        coEvery { checkoutSdk.executeCheckout(any(), any(), any(), any()) } returns true
        coEvery { checkoutSdk.submitPayment(any(), any(), any()) } returns true

        val viewModel = CheckoutViewModel(checkoutSdk)
        val themeConfig = CheckoutThemeConfig()

        // Act
        composeTestRule.setContent {
            CheckoutScreen(
                amount = 299.99,
                currency = "USD",
                orderTitle = "Caribbean Cruise",
                orderDescription = "7 Nights",
                viewModel = viewModel,
                themeConfig = themeConfig,
                stepsConfig = CheckoutStepsConfig(
                    showPartySize = false,
                    showRoomSelection = false,
                    showExtras = false,
                    showGuestInfo = false
                ),
                onNavigateBack = {}
            )
        }

        // Assert - Review Step is first because we disabled the others
        composeTestRule.onNodeWithText("Order Summary").assertIsDisplayed()
        composeTestRule.onNodeWithText("Caribbean Cruise").assertIsDisplayed()
        composeTestRule.onNodeWithText("Total: 299.99 USD").assertIsDisplayed()
    }

    @Test
    fun testCheckoutFlowNavigationAndPayment() {
        // Arrange
        val checkoutSdk = mockk<CheckoutSdk>(relaxed = true)
        val mockProcessor = mockk<PaymentProcessor>()
        every { mockProcessor.type } returns PaymentProcessorType.STRIPE
        
        every { checkoutSdk.enabledProcessors } returns listOf(mockProcessor)
        coEvery { checkoutSdk.executeCheckout(any(), any(), any(), any()) } returns true
        coEvery { checkoutSdk.submitPayment(any(), any(), any()) } returns true

        val viewModel = CheckoutViewModel(checkoutSdk)
        val themeConfig = CheckoutThemeConfig()

        var successCallbackInvoked = false

        // Act
        composeTestRule.setContent {
            CheckoutScreen(
                amount = 299.99,
                currency = "USD",
                orderTitle = "Caribbean Cruise",
                orderDescription = "7 Nights",
                viewModel = viewModel,
                themeConfig = themeConfig,
                stepsConfig = CheckoutStepsConfig(
                    showPartySize = false,
                    showRoomSelection = false,
                    showExtras = false,
                    showGuestInfo = false
                ),
                onNavigateBack = {},
                onCheckoutSuccess = {
                    successCallbackInvoked = true
                }
            )
        }

        // Step 1: Review
        composeTestRule.onNodeWithText("Order Summary").assertIsDisplayed()
        composeTestRule.onNodeWithText("Next").performClick()

        // Step 2: Pay
        composeTestRule.onNodeWithText("Select Payment Method").assertIsDisplayed()
        composeTestRule.onNodeWithText("Stripe").performClick()
        
        // After clicking the payment method, it should start processing and eventually succeed
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            successCallbackInvoked
        }
        assert(successCallbackInvoked)
    }
}
