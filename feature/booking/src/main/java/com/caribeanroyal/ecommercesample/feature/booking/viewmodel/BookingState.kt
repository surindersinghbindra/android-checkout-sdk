package com.caribeanroyal.ecommercesample.feature.booking.viewmodel

import com.caribeanroyal.ecommercesample.core.domain.model.CruiseItinerary

sealed class BookingState {
    object Loading : BookingState()
    data class Success(
        val itineraries: List<CruiseItinerary>
    ) : BookingState()
    data class Error(val message: String) : BookingState()
}
