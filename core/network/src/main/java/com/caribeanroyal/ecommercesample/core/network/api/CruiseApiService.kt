package com.caribeanroyal.ecommercesample.core.network.api

import com.caribeanroyal.ecommercesample.core.domain.model.CruiseItinerary
import retrofit2.http.GET
import retrofit2.http.Path

interface CruiseApiService {
    @GET("v1/itinerary/{packageCode}")
    suspend fun getItinerary(@Path("packageCode") packageCode: String): CruiseItinerary
    
    @GET("v1/cruises")
    suspend fun searchCruises(): List<CruiseItinerary>
}
