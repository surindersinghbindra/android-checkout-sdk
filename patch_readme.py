with open('README.md', 'r') as f:
    content = f.read()

sdk_section = """
## 🚀 SDK Integration & Initialization

The Checkout SDK uses a Builder pattern to safely instantiate the core engine, allowing configuration of active payment processors, debug modes, and analytics reporting.

### Basic Initialization
```kotlin
val sdkEngine = CheckoutSdk.Builder()
    .enableProcessors(listOf(PaymentProcessorType.STRIPE, PaymentProcessorType.ADYEN))
    .setEnvironment("production")
    .setDebuggable(BuildConfig.DEBUG) // Set to true to view SDK logs
    .build()
```

### Analytics & Error Reporting
We adhere to industry best practices by allowing host apps to inject their own logging and analytics trackers. The SDK suppresses internal operational logs unless `debuggable` is true, but **critical errors** will always be dispatched to your provided `CheckoutAnalytics` interface.

```kotlin
val tracker = object : CheckoutAnalytics {
    override fun logEvent(eventName: String, params: Map<String, Any>) {
        // e.g. FirebaseAnalytics.getInstance(context).logEvent(eventName, bundle)
    }

    override fun logError(throwable: Throwable, message: String) {
        // e.g. FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}

val sdkEngine = CheckoutSdk.Builder()
    .enableProcessors(listOf(PaymentProcessorType.STRIPE))
    .setAnalytics(tracker)
    .build()
```

### Deprecations
- **`submitPayment()`**: As of recent updates, `submitPayment(amount, currency, processorType)` has been marked as `@Deprecated`. Please migrate directly to `executeCheckout(...)`. The old method is retained purely for backwards compatibility and demonstration of our API deprecation strategy.

### ProGuard / R8 / Minification
The SDK ships with embedded `consumer-rules.pro` files out of the box. Host applications running minification (R8/ProGuard) do not need to configure any custom rules for this SDK. Public models are properly protected using the `@Keep` annotation to prevent obfuscation of public facing entry points.

---
"""

content = content.replace('---', sdk_section + '\n---', 1)

with open('README.md', 'w') as f:
    f.write(content)
