package com.caribeanroyal.ecommercesample.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Booking : Screen

    @Serializable
    data class Checkout(
        val price: Double,
        val currency: String
    ) : Screen
}
