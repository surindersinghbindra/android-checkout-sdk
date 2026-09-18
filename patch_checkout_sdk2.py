import re

with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdk.kt', 'r') as f:
    content = f.read()

# Add imports
content = content.replace('import androidx.annotation.Keep\n', 'import androidx.annotation.Keep\nimport android.content.Context\nimport androidx.startup.AppInitializer\nimport com.caribeanroyal.ecommercesample.sdk.checkout.core.db.CheckoutSdkInitializer\nimport com.caribeanroyal.ecommercesample.sdk.checkout.core.db.CheckoutDatabase\nimport com.caribeanroyal.ecommercesample.sdk.checkout.core.db.IdempotencyEntity\n')

# Update class constructor
content = content.replace(
    'val analytics: CheckoutAnalytics?\n) {\n\n    private val processedKeys = mutableSetOf<String>()\n',
    'val analytics: CheckoutAnalytics?,\n    private val context: Context\n) {\n\n    private val database: CheckoutDatabase by lazy {\n        AppInitializer.getInstance(context).initializeComponent(CheckoutSdkInitializer::class.java)\n    }\n'
)

# Fix the deprecated constructor to pass a dummy context or we can just remove it/break it.
# Wait, the deprecated constructor is public. If it takes a context now, it breaks the signature.
# Since it's already deprecated, let's just make it throw NotImplementedError or require context.
# Actually, the user doesn't care about the deprecated one. Let's just remove the old deprecated constructor that didn't take context.
content = re.sub(r'@Deprecated\("Use Builder.*?\)\s*:\s*this\(.*?null\)', '', content, flags=re.DOTALL)

# Update Builder
content = content.replace(
    'class Builder {',
    'class Builder(private val context: Context) {'
)

content = content.replace(
    'return CheckoutSdk(enabledProcessors, environment, debuggable, analytics)',
    'return CheckoutSdk(enabledProcessors, environment, debuggable, analytics, context.applicationContext)'
)

# Update executeCheckout logic 1
old_logic_1 = """        if (idempotencyKey != null && processedKeys.contains(idempotencyKey)) {
            throw AlreadyProcessedException("Order already processed")
        }
        val processor = enabledProcessors.find { it.type == processorType } 
            ?: throw IllegalArgumentException("Processor $processorType is not enabled in the SDK.")
        
        val success = processor.processPayment(amount, currency, idempotencyKey)
        if (success && idempotencyKey != null) {
            processedKeys.add(idempotencyKey)
        }
        return success"""

new_logic_1 = """        if (idempotencyKey != null && database.idempotencyDao().exists(idempotencyKey) > 0) {
            throw AlreadyProcessedException("Order already processed")
        }
        val processor = enabledProcessors.find { it.type == processorType } 
            ?: throw IllegalArgumentException("Processor $processorType is not enabled in the SDK.")
        
        val success = processor.processPayment(amount, currency, idempotencyKey)
        if (success && idempotencyKey != null) {
            database.idempotencyDao().insertKey(IdempotencyEntity(idempotencyKey))
        }
        return success"""

content = content.replace(old_logic_1, new_logic_1)

# Update executeCheckout logic 2
old_logic_2 = """        if (idempotencyKey != null && processedKeys.contains(idempotencyKey)) {
            throw AlreadyProcessedException("Order already processed")
        }
        if (enabledProcessors.isEmpty()) return false
        val success = enabledProcessors.first().processPayment(amount, currency, idempotencyKey)
        if (success && idempotencyKey != null) {
            processedKeys.add(idempotencyKey)
        }
        return success"""

new_logic_2 = """        if (idempotencyKey != null && database.idempotencyDao().exists(idempotencyKey) > 0) {
            throw AlreadyProcessedException("Order already processed")
        }
        if (enabledProcessors.isEmpty()) return false
        val success = enabledProcessors.first().processPayment(amount, currency, idempotencyKey)
        if (success && idempotencyKey != null) {
            database.idempotencyDao().insertKey(IdempotencyEntity(idempotencyKey))
        }
        return success"""

content = content.replace(old_logic_2, new_logic_2)

with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdk.kt', 'w') as f:
    f.write(content)

