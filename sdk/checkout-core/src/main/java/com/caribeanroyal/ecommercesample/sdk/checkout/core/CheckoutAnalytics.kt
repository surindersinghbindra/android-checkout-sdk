package com.caribeanroyal.ecommercesample.sdk.checkout.core

import androidx.annotation.Keep

@Keep
interface CheckoutAnalytics {
    fun logEvent(eventName: String, params: Map<String, Any> = emptyMap())
    fun logError(throwable: Throwable, message: String)
}
