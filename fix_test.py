with open('sdk/checkout-ui/src/androidTest/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutScreenTest.kt', 'r') as f:
    content = f.read()

content = content.replace('every { mockProcessor.name } returns "Stripe"', '')

with open('sdk/checkout-ui/src/androidTest/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutScreenTest.kt', 'w') as f:
    f.write(content)
