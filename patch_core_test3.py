with open('sdk/checkout-core/src/test/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdkTest.kt', 'r') as f:
    content = f.read()

content = content.replace('any<Class<*>>()', 'any()')

with open('sdk/checkout-core/src/test/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdkTest.kt', 'w') as f:
    f.write(content)
