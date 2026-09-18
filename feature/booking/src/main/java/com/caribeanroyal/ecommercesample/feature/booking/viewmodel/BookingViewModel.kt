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
        val itinerary: CruiseItinerary
    ) : BookingState() {
        val finalPrice: Double get() = itinerary.basePrice
    }
    data class Error(val message: String) : BookingState()
}

class BookingViewModel(
    private val getCruiseItineraryUseCase: GetCruiseItineraryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<BookingState>(BookingState.Loading)
    val state = _state.asStateFlow()

    init {
        fetchItinerary()
    }

    private fun fetchItinerary() {
        viewModelScope.launch {
            getCruiseItineraryUseCase("OY07M869")
                .onSuccess { cruise ->
                    _state.value = BookingState.Success(cruise)
                }
                .onFailure { error ->
                    _state.value = BookingState.Error(error.message ?: "Failed to load itinerary")
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
