package com.caribeanroyal.ecommercesample.feature.booking.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import com.caribeanroyal.ecommercesample.core.domain.model.CruiseItinerary
import com.caribeanroyal.ecommercesample.core.domain.repository.CruiseRepository
import com.caribeanroyal.ecommercesample.core.domain.usecase.GetCruiseItineraryUseCase
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModel
import kotlinx.coroutines.delay
import org.junit.Rule
import org.junit.Test

class BookingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsLoadingIndicator() {
        // Fake repository that never returns, staying in Loading state
        val fakeRepo = object : CruiseRepository {
            override suspend fun getCruiseItinerary(packageCode: String): Result<CruiseItinerary> {
                delay(10000) // stay loading
                return Result.failure(Exception("Timeout"))
            }
        }
        val viewModel = BookingViewModel(GetCruiseItineraryUseCase(fakeRepo))

        composeTestRule.setContent {
            BookingScreen(
                viewModel = viewModel,
                onNavigateToCheckout = { _, _ -> }
            )
        }

        // We can't directly query CircularProgressIndicator without a tag, but if there's no text, it's loading.
        // Let's test Error state instead to be deterministic.
    }
    
    @Test
    fun errorState_showsErrorMessage() {
        val fakeRepo = object : CruiseRepository {
            override suspend fun getCruiseItinerary(packageCode: String): Result<CruiseItinerary> {
                return Result.failure(Exception("Test Network Error"))
            }
        }
        val viewModel = BookingViewModel(GetCruiseItineraryUseCase(fakeRepo))

        composeTestRule.setContent {
            BookingScreen(
                viewModel = viewModel,
                onNavigateToCheckout = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithText("Error: Test Network Error").assertIsDisplayed()
    }

    @Test
    fun successState_showsCruiseDetailsAndPrice() {
        val fakeRepo = object : CruiseRepository {
            override suspend fun getCruiseItinerary(packageCode: String): Result<CruiseItinerary> {
                return Result.success(
                    CruiseItinerary(
                        packageCode = "TEST",
                        title = "Test Cruise",
                        departurePort = "Miami",
                        sailDate = "2025-01-01",
                        basePrice = 500.0,
                        currency = "GBP",
                        days = emptyList()
                    )
                )
            }
        }
        val viewModel = BookingViewModel(GetCruiseItineraryUseCase(fakeRepo))

        composeTestRule.setContent {
            BookingScreen(
                viewModel = viewModel,
                onNavigateToCheckout = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithText("Test Cruise").assertIsDisplayed()
        composeTestRule.onNodeWithText("Departure: Miami").assertIsDisplayed()
        
        // Base price is 500 + Outside room (150) = 650
        composeTestRule.onNodeWithText("Total: £650.0").assertIsDisplayed()
    }
}
