package com.caribeanroyal.ecommercesample.core.domain.usecase

import com.caribeanroyal.ecommercesample.core.domain.model.CruiseItinerary
import com.caribeanroyal.ecommercesample.core.domain.repository.CruiseRepository
import javax.inject.Inject

class SearchCruisesUseCase @Inject constructor(
    private val repository: CruiseRepository
) {
    suspend operator fun invoke(): Result<List<CruiseItinerary>> {
        return repository.searchCruises()
    }
}
