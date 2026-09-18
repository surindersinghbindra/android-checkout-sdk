package com.caribeanroyal.ecommercesample.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockCruiseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath
        
        if (path.contains("v1/cruises")) {
            Thread.sleep(1000)
            val jsonResponse = """
                [
                    {
                      "packageCode": "OY07M869",
                      "title": "7 Night Greek Isles Cruise",
                      "departurePort": "Rome, Italy",
                      "sailDate": "2027-10-24",
                      "basePrice": 711.50,
                      "currency": "GBP",
                      "imageUrl": "https://images.unsplash.com/photo-1590523741831-ab7e8b8f9c7f?q=80&w=600&auto=format&fit=crop",
                      "days": [
                        { "dayNumber": 1, "location": "Rome, Italy", "isSeaDay": false }
                      ]
                    },
                    {
                      "packageCode": "SY07M542",
                      "title": "7 Night Bahamas Cruise",
                      "departurePort": "Miami, Florida",
                      "sailDate": "2027-11-15",
                      "basePrice": 599.00,
                      "currency": "USD",
                      "imageUrl": "https://images.unsplash.com/photo-1590523741831-ab7e8b8f9c7f?q=80&w=600&auto=format&fit=crop",
                      "days": [
                        { "dayNumber": 1, "location": "Miami, Florida", "isSeaDay": false }
                      ]
                    },
                    {
                      "packageCode": "WN07M123",
                      "title": "7 Night Caribbean Cruise",
                      "departurePort": "Orlando, Florida",
                      "sailDate": "2027-12-05",
                      "basePrice": 850.00,
                      "currency": "USD",
                      "imageUrl": "https://images.unsplash.com/photo-1590523741831-ab7e8b8f9c7f?q=80&w=600&auto=format&fit=crop",
                      "days": [
                        { "dayNumber": 1, "location": "Orlando, Florida", "isSeaDay": false }
                      ]
                    }
                ]
            """.trimIndent()

            return Response.Builder()
                .code(200)
                .message("OK")
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body(jsonResponse.toResponseBody("application/json".toMediaTypeOrNull()))
                .addHeader("content-type", "application/json")
                .build()
        } else if (path.contains("v1/itinerary")) {
            Thread.sleep(1000)
            val jsonResponse = """
                {
                  "packageCode": "OY07M869",
                  "title": "7 Night Greek Isles Cruise",
                  "departurePort": "Rome (Civitavecchia), Italy",
                  "sailDate": "2027-10-24",
                  "basePrice": 711.50,
                  "currency": "GBP",
                  "imageUrl": "https://images.unsplash.com/photo-1549887552-cb1071d3e5a5?q=80&w=600&auto=format&fit=crop",
                  "days": [
                    { "dayNumber": 1, "location": "Rome (Civitavecchia), Italy", "isSeaDay": false },
                    { "dayNumber": 2, "location": "Day at Sea", "isSeaDay": true },
                    { "dayNumber": 3, "location": "Santorini, Greece", "isSeaDay": false }
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

        return chain.proceed(request)
    }
}
