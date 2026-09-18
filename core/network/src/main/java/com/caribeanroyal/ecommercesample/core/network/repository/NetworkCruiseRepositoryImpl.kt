package com.caribeanroyal.ecommercesample.core.network.repository

import com.caribeanroyal.ecommercesample.core.domain.model.CruiseItinerary
import com.caribeanroyal.ecommercesample.core.domain.repository.CruiseRepository
import com.caribeanroyal.ecommercesample.core.network.api.CruiseApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkCruiseRepositoryImpl @Inject constructor(
    private val apiService: CruiseApiService
) : CruiseRepository {
    
    override suspend fun getCruiseItinerary(packageCode: String): Result<CruiseItinerary> {
        return withContext(Dispatchers.IO) {
            try {
                // This makes the actual Retrofit HTTP call!
                val itinerary = apiService.getItinerary(packageCode)
                Result.success(itinerary)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    override suspend fun searchCruises(): Result<List<CruiseItinerary>> {
        return withContext(Dispatchers.IO) {
            try {
                val cruises = apiService.searchCruises()
                Result.success(cruises)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
