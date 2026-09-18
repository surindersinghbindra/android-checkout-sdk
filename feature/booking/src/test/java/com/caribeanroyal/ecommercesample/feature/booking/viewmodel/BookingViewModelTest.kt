package com.caribeanroyal.ecommercesample.feature.booking.viewmodel

import app.cash.turbine.test
import com.caribeanroyal.ecommercesample.core.domain.model.CruiseItinerary
import com.caribeanroyal.ecommercesample.core.domain.usecase.GetCruiseItineraryUseCase
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getCruiseItineraryUseCase: GetCruiseItineraryUseCase
    private lateinit var viewModel: BookingViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getCruiseItineraryUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when initialization starts, state is Loading`() = runTest {
        coEvery { getCruiseItineraryUseCase(any()) } returns Result.success(mockItinerary)

        viewModel = BookingViewModel(getCruiseItineraryUseCase)

        viewModel.state.test {
            val initialState = awaitItem()
            assertTrue(initialState is BookingState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when use case succeeds, state becomes Success`() = runTest {
        coEvery { getCruiseItineraryUseCase(any()) } returns Result.success(mockItinerary)

        viewModel = BookingViewModel(getCruiseItineraryUseCase)

        viewModel.state.test {
            awaitItem() // Loading
            val successState = awaitItem() as BookingState.Success
            assertEquals("7 Night Greek Isles Cruise", successState.itinerary.title)
            assertEquals(1328.0, successState.finalPrice, 0.0)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when use case fails, state becomes Error`() = runTest {
        coEvery { getCruiseItineraryUseCase(any()) } returns Result.failure(Exception("Network error"))

        viewModel = BookingViewModel(getCruiseItineraryUseCase)

        viewModel.state.test {
            awaitItem() // Loading
            val errorState = awaitItem() as BookingState.Error
            assertEquals("Network error", errorState.message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private val mockItinerary = CruiseItinerary(
        title = "7 Night Greek Isles Cruise",
        departurePort = "Rome",
        packageCode = "OY07M869",
        sailDate = "24 Oct 2027",
        basePrice = 1328.0,
        currency = "GBP",
        days = emptyList()
    )
}
