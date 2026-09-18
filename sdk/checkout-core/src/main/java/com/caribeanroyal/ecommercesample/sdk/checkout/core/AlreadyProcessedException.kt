package com.caribeanroyal.ecommercesample.sdk.checkout.core

import androidx.annotation.Keep

/**
 * Exception thrown when a checkout request is made with an idempotency key 
 * that has already been successfully processed by the SDK.
 */
@Keep
class AlreadyProcessedException(message: String) : Exception(message)
