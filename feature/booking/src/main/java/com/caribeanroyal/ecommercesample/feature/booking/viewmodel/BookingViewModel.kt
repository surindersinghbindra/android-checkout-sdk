package com.caribeanroyal.ecommercesample.feature.booking.viewmodel

import androidx.lifecycle.ViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.caribeanroyal.ecommercesample.core.domain.usecase.SearchCruisesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookingViewModel(
    private val searchCruisesUseCase: SearchCruisesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<BookingState>(BookingState.Loading)
    val state: StateFlow<BookingState> = _state.asStateFlow()

    init {
        loadCruises()
    }

    private fun loadCruises() {
        viewModelScope.launch {
            _state.value = BookingState.Loading
            val result = searchCruisesUseCase()
            _state.value = if (result.isSuccess) {
                BookingState.Success(
                    itineraries = result.getOrThrow()
                )
            } else {
                BookingState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}

class BookingViewModelFactory @Inject constructor(
    private val searchCruisesUseCase: SearchCruisesUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookingViewModel(searchCruisesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
