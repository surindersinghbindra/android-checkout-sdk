with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/Processors.kt', 'r') as f:
    content = f.read()

fail_processor = """class FailProcessor : PaymentProcessor {
    override val type = PaymentProcessorType.FAIL_SIMULATOR

    override suspend fun processPayment(amount: Double, currency: String): Boolean {
        println("Processing $amount $currency via internal FAIL simulator")
        delay(1000)
        return false // Simulate failure
    }
}
"""

content += "\n" + fail_processor

with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/Processors.kt', 'w') as f:
    f.write(content)
