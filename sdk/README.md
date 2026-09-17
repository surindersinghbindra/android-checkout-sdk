# Checkout SDK

Welcome to the internal, white-label Checkout SDK. This library provides a highly scalable and backward-compatible e-commerce checkout solution meant to be integrated across all our brand applications.

## Architecture

This SDK is split into two modules:
*   `:sdk:checkout-core`: A pure Kotlin module handling domain logic and Strategy patterns (e.g., `PaymentProcessor`). It has zero Android UI dependencies.
*   `:sdk:checkout-ui`: An Android library providing Jetpack Compose visual elements, driven by an MVI state architecture.

### White-Labeling Strategy
We use runtime dynamic configuration via the `CheckoutThemeConfig` data class instead of static XML resources.
When launching the SDK, you provide the `CheckoutThemeConfig` populated with colors derived from the host application's current flavor/theme.

## Semantic Versioning (SemVer)
We follow strict SemVer (MAJOR.MINOR.PATCH):
*   **MAJOR**: Breaking changes. (Avoid these. We prefer to use `@JvmOverloads` and `@Deprecated` to keep old methods working).
*   **MINOR**: New backward-compatible features (e.g., adding a new field to `CheckoutState` with a default value).
*   **PATCH**: Backward-compatible bug fixes.

## API Compatibility & BCV
We use the **Kotlin Binary Compatibility Validator (BCV)** to enforce API contracts. 
*   If you add or modify a public function, you MUST run `./gradlew apiDump` to generate the updated `.api` files and commit them. 
*   CI will automatically run `./gradlew apiCheck` on all Pull Requests to ensure you haven't accidentally broken existing consumers.

## Usage in App

Initialize the `CheckoutSdk` in your application or DI graph using the `Builder`:

```kotlin
val sdk = CheckoutSdk.Builder()
    .setPaymentProcessor(StripePaymentProcessor()) // Pass your implementation
    .setEnvironment("production")
    .build()
```

Provide the dynamic theme configuration:

```kotlin
val sdkThemeConfig = CheckoutThemeConfig(
    primaryColor = BrandPrimary, // Pulled from your host app's design system
    secondaryColor = BrandSecondary,
    buttonCornerRadiusDp = 12
)
```

Finally, use the `CheckoutScreen` in your Compose hierarchy:

```kotlin
// Instantiate the ViewModel using the SDK instance
val factory = CheckoutViewModelFactory(checkoutSdk)
val checkoutViewModel = ViewModelProvider(viewModelStoreOwner, factory)[CheckoutViewModel::class.java]

CheckoutScreen(
    viewModel = checkoutViewModel,
    themeConfig = checkoutThemeConfig,
    onNavigateBack = { /* Handle back navigation */ }
)
```
