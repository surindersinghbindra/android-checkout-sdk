with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/PaymentProcessor.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'suspend fun processPayment(amount: Double, currency: String): Boolean',
    'suspend fun processPayment(amount: Double, currency: String, idempotencyKey: String? = null): Boolean'
)

with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/PaymentProcessor.kt', 'w') as f:
    f.write(content)

with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/Processors.kt', 'r') as f:
    content2 = f.read()

content2 = content2.replace(
    'suspend fun processPayment(amount: Double, currency: String): Boolean',
    'suspend fun processPayment(amount: Double, currency: String, idempotencyKey: String?): Boolean'
)

with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/Processors.kt', 'w') as f:
    f.write(content2)
