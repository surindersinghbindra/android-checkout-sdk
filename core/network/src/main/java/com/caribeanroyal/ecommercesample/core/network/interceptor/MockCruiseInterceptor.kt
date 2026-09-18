package com.caribeanroyal.ecommercesample.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockCruiseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        
        // Only intercept itinerary requests
        if (request.url.encodedPath.contains("v1/itinerary")) {
            
            // Simulate network latency
            Thread.sleep(1000)
            
            val jsonResponse = """
                {
                  "packageCode": "OY07M869",
                  "title": "7 Night Greek Isles Cruise",
                  "departurePort": "Rome (Civitavecchia), Italy",
                  "sailDate": "2027-10-24",
                  "basePrice": 711.50,
                  "currency": "GBP",
                  "days": [
                    { "dayNumber": 1, "location": "Rome (Civitavecchia), Italy", "isSeaDay": false },
                    { "dayNumber": 2, "location": "Day at Sea", "isSeaDay": true },
                    { "dayNumber": 3, "location": "Santorini, Greece", "isSeaDay": false },
                    { "dayNumber": 4, "location": "Ephesus (Kusadasi), Turkey", "isSeaDay": false },
                    { "dayNumber": 5, "location": "Mykonos, Greece", "isSeaDay": false },
                    { "dayNumber": 6, "location": "Day at Sea", "isSeaDay": true },
                    { "dayNumber": 7, "location": "Naples/Capri, Italy", "isSeaDay": false },
                    { "dayNumber": 8, "location": "Rome (Civitavecchia), Italy", "isSeaDay": false }
                  ]
                }
            """.trimIndent()

            return Response.Builder()
                .code(200)
                .message("OK")
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body(jsonResponse.toResponseBody("application/json".toMediaTypeOrNull()))
                .addHeader("content-type", "application/json")
                .build()
        }

        // Pass through any other requests (won't happen in our app, but good practice)
        return chain.proceed(request)
    }
}
