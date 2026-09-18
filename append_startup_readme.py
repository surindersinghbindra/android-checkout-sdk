with open('README.md', 'r') as f:
    content = f.read()

startup_docs = """
### Deferred Jetpack App Startup & Room DB
To ensure the SDK does not bloat the host app's Time-To-Interactive (TTI) during cold starts, the SDK integrates **Jetpack App Startup** but recommends a deferred initialization strategy. 

The SDK internally provides `CheckoutSdkInitializer`, which provisions the internal Room Database for idempotency keys. Host apps are configured to intentionally disable auto-initialization via Manifest merging:

```xml
<provider
    android:name="androidx.startup.InitializationProvider"
    android:authorities="${applicationId}.androidx-startup"
    android:exported="false"
    tools:node="merge">
    
    <!-- Explicitly disable the specific SDK initializer -->
    <meta-data
        android:name="com.caribeanroyal.ecommercesample.sdk.checkout.core.db.CheckoutSdkInitializer"
        tools:node="remove" />
</provider>
```

When the user actively enters the Checkout Flow (e.g. inside `CheckoutGraph.kt`), the host app manually initializes the component Just-In-Time:
```kotlin
AppInitializer.getInstance(context)
    .initializeComponent(CheckoutSdkInitializer::class.java)
```
This is a highly recommended best practice for SDKs requiring heavyweight components like Room.
"""

content = content.replace('### Deprecations\n', startup_docs + '\n### Deprecations\n')

with open('README.md', 'w') as f:
    f.write(content)
