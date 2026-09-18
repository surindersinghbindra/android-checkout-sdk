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

    private lateinit var viewModel: BookingViewModel
    private val useCase: GetCruiseItineraryUseCase = mockk()
    
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading and transitions to Success when fetch succeeds`() = runTest {
        val mockItinerary = CruiseItinerary(
            packageCode = "TEST",
            title = "Test Cruise",
            departurePort = "Miami",
            sailDate = "2025-01-01",
            basePrice = 500.0,
            currency = "USD",
            days = emptyList()
        )

        coEvery { useCase(any()) } returns Result.success(mockItinerary)

        viewModel = BookingViewModel(useCase)

        viewModel.state.test {
            // First emission is Loading
            assertTrue(awaitItem() is BookingState.Loading)
            
            // Second emission is Success
            val successState = awaitItem() as BookingState.Success
            assertEquals("Test Cruise", successState.itinerary.title)
            assertEquals("Outside", successState.selectedRoom) // default
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial state is Loading and transitions to Error when fetch fails`() = runTest {
        coEvery { useCase(any()) } returns Result.failure(Exception("Network Error"))

        viewModel = BookingViewModel(useCase)

        viewModel.state.test {
            assertTrue(awaitItem() is BookingState.Loading)
            
            val errorState = awaitItem() as BookingState.Error
            assertEquals("Network Error", errorState.message)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `selectRoom updates the selected room in Success state`() = runTest {
        val mockItinerary = CruiseItinerary(
            packageCode = "TEST",
            title = "Test Cruise",
            departurePort = "Miami",
            sailDate = "2025-01-01",
            basePrice = 500.0,
            currency = "USD",
            days = emptyList()
        )

        coEvery { useCase(any()) } returns Result.success(mockItinerary)

        viewModel = BookingViewModel(useCase)

        viewModel.state.test {
            awaitItem() // Loading
            val success = awaitItem() as BookingState.Success
            assertEquals("Outside", success.selectedRoom) // Default
            assertEquals(650.0, success.finalPrice, 0.0) // 500 + 150 (Outside)

            // Trigger action
            viewModel.selectRoom("Balcony")

            val updatedState = awaitItem() as BookingState.Success
            assertEquals("Balcony", updatedState.selectedRoom)
            assertEquals(800.0, updatedState.finalPrice, 0.0) // 500 + 300 (Balcony)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
}
