with open('sdk/checkout-ui/src/test/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutViewModelTest.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'checkoutSdk.executeCheckout(100.0, "USD", PaymentProcessorType.STRIPE)',
    'checkoutSdk.executeCheckout(100.0, "USD", PaymentProcessorType.STRIPE, any())'
)

with open('sdk/checkout-ui/src/test/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutViewModelTest.kt', 'w') as f:
    f.write(content)
