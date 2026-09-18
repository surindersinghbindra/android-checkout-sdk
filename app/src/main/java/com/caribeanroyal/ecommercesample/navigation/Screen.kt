package com.caribeanroyal.ecommercesample.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Splash : Screen

    @Serializable
    data object Booking : Screen

    @Serializable
    data class CruiseDetail(
        val packageCode: String
    ) : Screen

    @Serializable
    data class Checkout(
        val price: Double,
        val currency: String,
        val orderTitle: String,
        val orderDescription: String
    ) : Screen
}
