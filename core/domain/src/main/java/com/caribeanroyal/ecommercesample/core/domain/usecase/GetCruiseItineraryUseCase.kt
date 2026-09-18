package com.caribeanroyal.ecommercesample.core.domain.usecase

import com.caribeanroyal.ecommercesample.core.domain.model.CruiseItinerary
import com.caribeanroyal.ecommercesample.core.domain.repository.CruiseRepository
import javax.inject.Inject

class GetCruiseItineraryUseCase @Inject constructor(
    private val repository: CruiseRepository
) {
    suspend operator fun invoke(packageCode: String): Result<CruiseItinerary> {
        return repository.getCruiseItinerary(packageCode)
    }
}
