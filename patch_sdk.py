with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdk.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'package com.caribeanroyal.ecommercesample.sdk.checkout.core',
    'package com.caribeanroyal.ecommercesample.sdk.checkout.core\n\nimport androidx.annotation.Keep'
)

content = content.replace(
    'class CheckoutSdk private constructor(',
    '@Keep\nclass CheckoutSdk private constructor('
)

content = content.replace(
    'val environment: String',
    'val environment: String,\n    val debuggable: Boolean,\n    val analytics: CheckoutAnalytics?'
)

content = content.replace(
    'constructor(paymentProcessor: PaymentProcessor) : this(listOf(paymentProcessor), "production")',
    'constructor(paymentProcessor: PaymentProcessor) : this(listOf(paymentProcessor), "production", false, null)'
)

content = content.replace(
    'class Builder {',
    '@Keep\n    class Builder {'
)

content = content.replace(
    'private var environment: String = "production"',
    'private var environment: String = "production"\n        private var debuggable: Boolean = false\n        private var analytics: CheckoutAnalytics? = null'
)

content = content.replace(
    'fun setEnvironment(env: String = "production") = apply { this.environment = env }',
    'fun setEnvironment(env: String = "production") = apply { this.environment = env }\n\n        fun setDebuggable(debuggable: Boolean) = apply { this.debuggable = debuggable }\n\n        fun setAnalytics(analytics: CheckoutAnalytics) = apply { this.analytics = analytics }'
)

content = content.replace(
    'return CheckoutSdk(enabledProcessors, environment)',
    'return CheckoutSdk(enabledProcessors, environment, debuggable, analytics)'
)

# Add logging methods
logging_code = """    fun logEvent(tag: String, message: String) {
        if (debuggable) {
            println("[$tag] $message")
        }
    }

    fun logError(tag: String, message: String, throwable: Throwable? = null) {
        if (debuggable) {
            println("[$tag] ERROR: $message")
            throwable?.printStackTrace()
        }
        val t = throwable ?: Exception(message)
        analytics?.logError(t, "[$tag] $message")
    }

    @Deprecated("""

content = content.replace('    @Deprecated(', logging_code)

with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdk.kt', 'w') as f:
    f.write(content)
