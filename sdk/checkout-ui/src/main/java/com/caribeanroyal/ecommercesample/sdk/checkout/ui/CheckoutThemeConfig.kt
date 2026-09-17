package com.caribeanroyal.ecommercesample.sdk.checkout.ui

import androidx.compose.ui.graphics.Color

/**
 * Runtime configuration for white-labeling the SDK UI.
 * Avoids static XML resources, allowing dynamic rebranding.
 */
data class CheckoutThemeConfig @JvmOverloads constructor(
    val primaryColor: Color = Color(0xFF6200EE),
    val secondaryColor: Color = Color(0xFF03DAC5),
    val buttonCornerRadiusDp: Int = 8
)
