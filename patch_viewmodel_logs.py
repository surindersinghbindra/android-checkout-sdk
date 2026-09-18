with open('sdk/checkout-ui/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutViewModel.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'sdkEngine.logError("CheckoutViewModel", "Payment already processed")',
    'sdkEngine.logEvent("CheckoutViewModel", "Payment already processed (Idempotency key matched)")'
)

with open('sdk/checkout-ui/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutViewModel.kt', 'w') as f:
    f.write(content)
