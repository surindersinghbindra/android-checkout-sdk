with open('sdk/checkout-ui/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutViewModel.kt', 'r') as f:
    content = f.read()

# Add imports
content = content.replace('import kotlinx.coroutines.flow.asStateFlow\n', 'import kotlinx.coroutines.flow.asStateFlow\nimport com.caribeanroyal.ecommercesample.sdk.checkout.core.AlreadyProcessedException\nimport java.util.UUID\n')

# Add sessionKey inside ViewModel
content = content.replace(
    'private val sdkEngine: CheckoutSdk\n) : ViewModel() {',
    'private val sdkEngine: CheckoutSdk\n) : ViewModel() {\n\n    private val sessionKey = UUID.randomUUID().toString()\n'
)

# Update the launch block to use sessionKey and catch AlreadyProcessedException
old_launch = """        viewModelScope.launch {
            try {
                // We use the deprecated method intentionally here to show warnings to consumers
                val success = sdkEngine.submitPayment(amount, currency, processorType)
                if (success) {
                    sdkEngine.logEvent("CheckoutViewModel", "Payment processed successfully via $processorType")
                    _uiState.value = CheckoutState.Success
                } else {
                    sdkEngine.logError("CheckoutViewModel", "Payment failed via $processorType")
                    _uiState.value = CheckoutState.Error("Payment declined by processor.")
                }
            } catch (e: Exception) {
                sdkEngine.logError("CheckoutViewModel", "Exception during payment", e)
                _uiState.value = CheckoutState.Error(e.message ?: "An unexpected error occurred.")
            }
        }"""

new_launch = """        viewModelScope.launch {
            try {
                // Calling the modern executeCheckout method and passing the idempotency key to prevent double bookings
                val success = sdkEngine.executeCheckout(amount, currency, processorType, sessionKey)
                if (success) {
                    sdkEngine.logEvent("CheckoutViewModel", "Payment processed successfully via $processorType")
                    _uiState.value = CheckoutState.Success
                } else {
                    sdkEngine.logError("CheckoutViewModel", "Payment failed via $processorType")
                    _uiState.value = CheckoutState.Error("Payment declined by processor.")
                }
            } catch (e: AlreadyProcessedException) {
                sdkEngine.logError("CheckoutViewModel", "Idempotency violation", e)
                _uiState.value = CheckoutState.Error("This order has already been placed.")
            } catch (e: Exception) {
                sdkEngine.logError("CheckoutViewModel", "Exception during payment", e)
                _uiState.value = CheckoutState.Error(e.message ?: "An unexpected error occurred.")
            }
        }"""

content = content.replace(old_launch, new_launch)

with open('sdk/checkout-ui/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutViewModel.kt', 'w') as f:
    f.write(content)
