package com.caribeanroyal.ecommercesample.feature.booking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.caribeanroyal.ecommercesample.core.domain.model.CruiseItinerary
import com.caribeanroyal.ecommercesample.core.domain.usecase.GetCruiseItineraryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class BookingState {
    object Loading : BookingState()
    data class Success(
        val itinerary: CruiseItinerary,
        val selectedRoom: String = "Outside",
        val roomPrices: Map<String, Double> = mapOf(
            "Interior" to 0.0,
            "Outside" to 150.0,
            "Balcony" to 300.0,
            "Suite" to 800.0
        )
    ) : BookingState() {
        val finalPrice: Double get() = itinerary.basePrice + (roomPrices[selectedRoom] ?: 0.0)
    }
    data class Error(val message: String) : BookingState()
}

class BookingViewModel(
    private val getCruiseItineraryUseCase: GetCruiseItineraryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<BookingState>(BookingState.Loading)
    val state: StateFlow<BookingState> = _state.asStateFlow()

    init {
        loadItinerary("OY07M869")
    }

    private fun loadItinerary(packageCode: String) {
        viewModelScope.launch {
            _state.value = BookingState.Loading
            getCruiseItineraryUseCase(packageCode)
                .onSuccess { itinerary ->
                    _state.value = BookingState.Success(itinerary = itinerary)
                }
                .onFailure { error ->
                    _state.value = BookingState.Error(error.message ?: "Failed to load itinerary")
                }
        }
    }

    fun selectRoom(roomType: String) {
        _state.update { currentState ->
            if (currentState is BookingState.Success) {
                currentState.copy(selectedRoom = roomType)
            } else {
                currentState
            }
        }
    }
}

class BookingViewModelFactory(
    private val useCase: GetCruiseItineraryUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BookingViewModel(useCase) as T
    }
}
