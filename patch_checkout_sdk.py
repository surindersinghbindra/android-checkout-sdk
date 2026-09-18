with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdk.kt', 'r') as f:
    content = f.read()

# Add processedKeys tracker inside CheckoutSdk class body
content = content.replace(
    'class CheckoutSdk private constructor(',
    'class CheckoutSdk private constructor('
)
content = content.replace(
    'val analytics: CheckoutAnalytics?\n) {',
    'val analytics: CheckoutAnalytics?\n) {\n\n    private val processedKeys = mutableSetOf<String>()\n'
)

# Update executeCheckout signatures
content = content.replace(
    'suspend fun executeCheckout(amount: Double, currency: String, processorType: PaymentProcessorType): Boolean {',
    'suspend fun executeCheckout(amount: Double, currency: String, processorType: PaymentProcessorType, idempotencyKey: String? = null): Boolean {'
)

old_logic_1 = """        val processor = enabledProcessors.find { it.type == processorType } 
            ?: throw IllegalArgumentException("Processor $processorType is not enabled in the SDK.")
        return processor.processPayment(amount, currency)"""

new_logic_1 = """        if (idempotencyKey != null && processedKeys.contains(idempotencyKey)) {
            throw AlreadyProcessedException("Order already processed")
        }
        val processor = enabledProcessors.find { it.type == processorType } 
            ?: throw IllegalArgumentException("Processor $processorType is not enabled in the SDK.")
        
        val success = processor.processPayment(amount, currency, idempotencyKey)
        if (success && idempotencyKey != null) {
            processedKeys.add(idempotencyKey)
        }
        return success"""

content = content.replace(old_logic_1, new_logic_1)

# And the deprecated one:
content = content.replace(
    'suspend fun executeCheckout(amount: Double, currency: String): Boolean {',
    'suspend fun executeCheckout(amount: Double, currency: String, idempotencyKey: String? = null): Boolean {'
)

old_logic_2 = """        if (enabledProcessors.isEmpty()) return false
        return enabledProcessors.first().processPayment(amount, currency)"""

new_logic_2 = """        if (idempotencyKey != null && processedKeys.contains(idempotencyKey)) {
            throw AlreadyProcessedException("Order already processed")
        }
        if (enabledProcessors.isEmpty()) return false
        val success = enabledProcessors.first().processPayment(amount, currency, idempotencyKey)
        if (success && idempotencyKey != null) {
            processedKeys.add(idempotencyKey)
        }
        return success"""

content = content.replace(old_logic_2, new_logic_2)

with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdk.kt', 'w') as f:
    f.write(content)

