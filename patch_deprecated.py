with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdk.kt', 'r') as f:
    content = f.read()

deprecated_method = """    @Deprecated(
        message = "This method will be replaced in the future to standardise naming conventions.",
        replaceWith = ReplaceWith("executeCheckout(amount, currency, processorType)")
    )
    suspend fun submitPayment(amount: Double, currency: String, processorType: PaymentProcessorType): Boolean {
        return executeCheckout(amount, currency, processorType)
    }

    /**"""

content = content.replace('    /**', deprecated_method, 1)

with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdk.kt', 'w') as f:
    f.write(content)
