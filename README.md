# E-Commerce App & Checkout SDK Monorepo

Welcome to the E-Commerce  Android Monorepo. This repository contains the consumer-facing host applications and our internal, white-label Checkout SDK.

## Architecture Structure

- **`:app`**: The standard E-Commerce host application using Jetpack Compose and our internal SDKs.
- **`:app-headless`**: A specialized host app that imports *only* the SDK core logic and builds its own custom UI.
- **`:sdk:checkout-core`**: Pure Kotlin backend for the SDK containing business logic, MVI intents, and the Strategy pattern for `PaymentProcessor`.
- **`:sdk:checkout-ui`**: Android UI library for the SDK containing Jetpack Compose visual elements and dynamic white-labeling configurations.


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


### Idempotency & Double-Booking Prevention
The SDK engine now natively guards against double-charging via Idempotency Keys. 
1. The host application (e.g. `CheckoutViewModel` in `checkout-ui` or a custom headless ViewModel) generates a single unique session key per checkout flow (e.g., `UUID`).
2. This key is passed down into `executeCheckout(amount, currency, processor, idempotencyKey)`.
3. If the user accidentally double-taps or a network retry happens, the SDK core will intercept the identical key, prevent the charge, and throw an `AlreadyProcessedException`.
4. The `CheckoutViewModel` catches this and smoothly updates the UI to show: *"This order has already been placed."*


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

### Deprecations
- **`submitPayment()`**: As of recent updates, `submitPayment(amount, currency, processorType)` has been marked as `@Deprecated`. Please migrate directly to `executeCheckout(...)`. The old method is retained purely for backwards compatibility and demonstration of our API deprecation strategy.

### ProGuard / R8 / Minification
The SDK ships with embedded `consumer-rules.pro` files out of the box. Host applications running minification (R8/ProGuard) do not need to configure any custom rules for this SDK. Public models are properly protected using the `@Keep` annotation to prevent obfuscation of public facing entry points.

---

---

## 🛡️ Binary Compatibility Validator (BCV)

As an enterprise SDK, we enforce strict backward compatibility. We use the **Kotlin Binary Compatibility Validator (BCV)** plugin to ensure we never accidentally break the public API contract for apps consuming our SDK.

### How it Works
1. **Public API Surface**: Any `public` class, function, or property in the SDK is part of the API surface.
2. **`.api` Files**: These are simple text files generated by the BCV plugin that act as a "snapshot" of all your public signatures. They are stored in `sdk/checkout-core/api/` and `sdk/checkout-ui/api/`.
3. **The CI Gate**: Every Pull Request runs `./gradlew apiCheck`. If you altered a method signature without updating the `.api` file, the CI will fail, protecting host apps from `NoSuchMethodError` crashes.

### BCV Commands
Whenever you intentionally add a new feature or method to the SDK, you must update the baseline snapshot before pushing to Git:
```bash
# Generates or updates the .api baseline files
./gradlew apiDump

# Validates the current code against the baseline (runs automatically in CI)
./gradlew apiCheck
```

---

## 📦 Publishing the SDK to GitHub Packages

We distribute this SDK internally using **GitHub Packages**. 

### Initial Setup (One-Time)
1. Generate a Personal Access Token (classic) on GitHub with `write:packages` and `read:packages` scopes.
2. Add the credentials to your local machine at `~/.gradle/gradle.properties`:
   ```properties
   gpr.user=surindersinghbindra
   gpr.key=ghp_YOUR_TOKEN_HERE
   ```

### Publishing Commands
```bash
# Test the build process locally (outputs to ~/.m2/ folder)
./gradlew publishToMavenLocal

# Publish the live .aar and .jar artifacts to the GitHub Packages registry
./gradlew publish
```

---

## 🛠️ Git Workflow Commands
When working in this monorepo, follow standard git flows. If you modify the SDK, remember to run `apiDump` before committing!

```bash
# Stage changes
git add .

# Commit changes
git commit -m "Your descriptive commit message"

# Push to the remote repository
git push
```


## 🏗️ E-Commerce SDK & App Architecture

## 1. Project Architecture & Modularization

We built a highly modular, multi-module Android project that acts as both a consumer application and a distributable SDK.

*   **Monorepo Strategy**: The project is broken down into distinct layers:
    *   `app` & `app-headless`: Host applications representing consumers of the SDK.
    *   `core:domain`, `core:database`, `core:network`: Pure logic, data persistence (Room), and remote networking (Retrofit).
    *   `feature:booking`: Standalone Jetpack Compose feature module containing the PLP (Product List) and PDP (Product Detail).
    *   `library:designsystem`: Centralized Compose theming, colors, typography, and reusable components.
    *   `sdk:checkout-core` (Pure JVM) & `sdk:checkout-ui` (Android Library): The distributable white-label commerce SDK.
*   **Dependency Injection**: Used **Dagger 2** (not Hilt) manually orchestrated via `AppComponent` in the `:app` module to prove an understanding of raw dependency graphs and factory provision.
*   **Edge-to-Edge UI**: Implemented modern Android windowing using `enableEdgeToEdge()` and Jetpack Compose `Scaffold` to draw UI behind system bars seamlessly.
*   **Modular Navigation**: Transitioned from a monolithic `NavHost` to a clean, decoupled approach using `NavGraphBuilder` extension functions (`bookingGraph`, `checkoutGraph`).
*   **SDK Builder Pattern**: Designed a robust `CheckoutSdk.Builder` that allows host apps to configure the SDK (Processors, Theme, Analytics, Debugging) cleanly.

---

## 2. Advanced Jetpack Compose Usage

The UI is entirely built in Jetpack Compose, showcasing modern declarative UI paradigms.

*   **State Hoisting & UDF (Unidirectional Data Flow)**: ViewModels expose immutable `StateFlow` objects. The UI consumes these flows via `collectAsState()` and dispatches actions/intents back to the ViewModel.
*   **Type-Safe Navigation**: Used the latest Jetpack Navigation Compose API (`navigation-compose:2.8.0`) with `@Serializable` Kotlin Data Objects/Classes to pass complex arguments safely between screens (e.g., `Screen.Checkout(price, currency)`).
*   **Custom Theming & Dynamic Styling**: Created a `CheckoutThemeConfig` that allows the host app to dynamically inject brand colors and corner radii into the SDK's internal Compose hierarchy.
*   **LaunchedEffect**: Used for side effects, such as the 1.5-second delay in the `SplashScreen` before navigating.

---

## 3. Kotlin Features & Idioms Used

We heavily leveraged modern Kotlin features to write concise, safe, and expressive code.

*   **Coroutines & Flows**: 
    *   Used `suspend` functions for asynchronous network and database calls.
    *   Used `StateFlow` and `MutableStateFlow` to manage UI state reactively.
    *   Used `viewModelScope.launch` to bind async work to the UI lifecycle.
*   **Sealed Classes & Interfaces**: 
    *   `sealed interface CheckoutState` (Idle, Processing, Success, Error) to strictly define the finite state machine of the UI.
    *   `sealed interface Screen` for exhaustive navigation routing.
*   **Extension Functions**: 
    *   `fun NavGraphBuilder.bookingGraph(...)` to extend the NavHost DSL without polluting the core class.
*   **Data Classes & Value Objects**: Used for immutable models like `CruiseModel`, `RoomModel`, and `CheckoutStepsConfig`.
*   **Delegated Properties**: Used `by remember { mutableStateOf(...) }` in Compose to delegate getter/setter logic to the Compose state engine.
*   **Higher-Order Functions & Lambdas**: Passed callbacks like `onNavigateToCheckout: (Double, String, String, String) -> Unit` from UI components up to the NavGraph to keep Composables completely decoupled from Navigation logic.
*   **Inline Classes & Enums**: `PaymentProcessorType` enum to strictly define supported gateways (Adyen, Stripe, Simulator).

---

## 4. Best Practices for SDK Development (The "Hardening" Phase)

Building an SDK requires stricter discipline than building an app. We implemented several industry-standard SDK practices:

*   **Binary Compatibility Validator (BCV)**: Used JetBrains BCV (`.api` dump files) to track the public API surface area and catch accidental breaking changes.
*   **ProGuard / R8 Rules**: Shipped a `consumer-rules.pro` file embedded in the `.aar` and used `@Keep` annotations on public models (`CheckoutThemeConfig`, etc.) to ensure the host app's minifier doesn't obfuscate critical SDK entry points.
*   **Graceful Deprecation**: Used `@Deprecated(..., ReplaceWith(...))` to guide developers away from old APIs (`submitPayment`) toward new ones (`executeCheckout`), emitting compile-time warnings instead of breaking their builds abruptly.
*   **Analytics & Error Boundaries**: Created a `CheckoutAnalytics` interface. Instead of the SDK assuming a specific analytics provider (like Firebase), it delegates `logEvent` and `logError` back to the host app, giving the consumer total control over data sovereignty.
*   **Silent by Default**: Implemented a `debuggable` flag. The SDK remains completely silent in production, only printing internal logs if the host explicitly opts in during debugging.

---

## 5. Testing Strategy

*   **Unit Testing with MockK**: Wrote isolated tests for `CheckoutViewModel` using `mockk` to stub the `CheckoutSdk`. Used `coEvery` to mock suspend functions.
*   **Turbine for Flow Testing**: Used Cash App's `Turbine` library (`viewModel.uiState.test { ... }`) to exhaustively assert the sequence of state emissions (Idle -> Processing -> Success) inside coroutines.
*   **Compose UI Testing**: Configured `androidx.ui.test.junit4` and `ComposeTestRule` to test UI nodes (`onNodeWithText`, `performClick`) in isolation, proving that the UI layer reacts correctly to mocked SDK responses.

---


## 6. Room DB Idempotency & Deferred App Startup

To protect against duplicate payment processing (double-booking), the SDK implements an idempotency layer using a local **Room Database**.

### Deferred Initialization (JIT)
To ensure the SDK does not impact the host application's cold start time (TTI), we utilize a deferred initialization strategy with Jetpack App Startup:
1.  **Block Auto-Init**: The SDK's Room Database initializer is explicitly disabled in the host app's `AndroidManifest.xml` using `tools:node="remove"`.
2.  **Lazy Loading**: The SDK's database is only constructed via `AppInitializer.getInstance(context).initializeComponent(...)` exactly when the user navigates to the checkout flow (Just-In-Time).

### Session Key Execution
When checking out, the UI generates a unique `sessionKey` (UUID) and passes it to `sdkEngine.executeCheckout(..., idempotencyKey = sessionKey)`. If the transaction has already been processed, the core SDK throws an `AlreadyProcessedException`, which the UI layer catches and handles gracefully.
