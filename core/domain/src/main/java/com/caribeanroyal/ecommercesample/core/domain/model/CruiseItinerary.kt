package com.caribeanroyal.ecommercesample.core.domain.model

data class CruiseItinerary(
    val packageCode: String,
    val title: String,
    val departurePort: String,
    val sailDate: String,
    val basePrice: Double,
    val currency: String,
    val imageUrl: String = "",
    val days: List<CruiseDay>
)

data class CruiseDay(
    val dayNumber: Int,
    val location: String,
    val isSeaDay: Boolean
)
