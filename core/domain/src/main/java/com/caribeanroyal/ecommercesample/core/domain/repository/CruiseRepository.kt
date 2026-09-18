package com.caribeanroyal.ecommercesample.core.domain.repository

import com.caribeanroyal.ecommercesample.core.domain.model.CruiseItinerary

interface CruiseRepository {
    suspend fun getCruiseItinerary(packageCode: String): Result<CruiseItinerary>
    suspend fun searchCruises(): Result<List<CruiseItinerary>>
}
