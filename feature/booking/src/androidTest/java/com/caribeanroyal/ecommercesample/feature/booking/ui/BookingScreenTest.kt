package com.caribeanroyal.ecommercesample.feature.booking.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.caribeanroyal.ecommercesample.core.domain.model.CruiseItinerary
import com.caribeanroyal.ecommercesample.core.domain.usecase.SearchCruisesUseCase
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BookingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit val searchCruisesUseCase: SearchCruisesUseCase
    private lateinit val viewModel: BookingViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        searchCruisesUseCase = mockk()
    }

    @Test
    fun screenShowsLoadingInitially() {
        coEvery { searchCruisesUseCase() } coAnswers {
            kotlinx.coroutines.delay(1000)
            Result.success(emptyList())
        }
        viewModel = BookingViewModel(searchCruisesUseCase)

        composeTestRule.setContent {
            BookingScreen(
                viewModel = viewModel,
                onNavigateToCheckout = { _, _, _, _ -> }
            )
        }
    }

    @Test
    fun screenShowsCruiseListOnSuccess() = runBlocking {
        coEvery { searchCruisesUseCase() } returns Result.success(listOf(mockItinerary))
        viewModel = BookingViewModel(searchCruisesUseCase)

        composeTestRule.setContent {
            BookingScreen(
                viewModel = viewModel,
                onNavigateToCheckout = { _, _, _, _ -> }
            )
        }

        composeTestRule.onNodeWithText("7 Night Greek Isles Cruise").assertExists()
        composeTestRule.onNodeWithText("Start Booking").assertExists()
    }

    @Test
    fun screenShowsErrorOnFailure() = runBlocking {
        coEvery { searchCruisesUseCase() } returns Result.failure(Exception("Network Error"))
        viewModel = BookingViewModel(searchCruisesUseCase)

        composeTestRule.setContent {
            BookingScreen(
                viewModel = viewModel,
                onNavigateToCheckout = { _, _, _, _ -> }
            )
        }

        composeTestRule.onNodeWithText("Error: Network Error").assertExists()
    }

    private val mockItinerary = CruiseItinerary(
        title = "7 Night Greek Isles Cruise",
        departurePort = "Rome",
        packageCode = "OY07M869",
        sailDate = "24 Oct 2027",
        basePrice = 1328.0,
        currency = "GBP",
        imageUrl = "",
        days = emptyList()
    )
}
