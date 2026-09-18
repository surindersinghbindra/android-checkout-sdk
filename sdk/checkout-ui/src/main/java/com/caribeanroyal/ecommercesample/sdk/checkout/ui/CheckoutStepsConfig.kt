package com.caribeanroyal.ecommercesample.sdk.checkout.ui

data class CheckoutStepsConfig @JvmOverloads constructor(
    val showPartySize: Boolean = true,
    val showRoomSelection: Boolean = true,
    val showExtras: Boolean = true,
    val showGuestInfo: Boolean = true
)
