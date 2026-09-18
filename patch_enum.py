with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/PaymentProcessor.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'HEADLESS("Custom Headless")',
    'HEADLESS("Custom Headless"),\n    FAIL_SIMULATOR("Simulate Failure (Demo)")'
)

with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/PaymentProcessor.kt', 'w') as f:
    f.write(content)
