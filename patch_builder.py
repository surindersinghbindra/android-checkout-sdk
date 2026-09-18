with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdk.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'PaymentProcessorType.STRIPE -> enabledProcessors.add(StripeProcessor())',
    'PaymentProcessorType.STRIPE -> enabledProcessors.add(StripeProcessor())\n                    PaymentProcessorType.FAIL_SIMULATOR -> enabledProcessors.add(FailProcessor())'
)

with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdk.kt', 'w') as f:
    f.write(content)
