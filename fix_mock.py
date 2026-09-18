with open('sdk/checkout-ui/src/test/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutViewModelTest.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'private val checkoutSdk = mockk<CheckoutSdk>()',
    'private val checkoutSdk = mockk<CheckoutSdk>(relaxed = true)'
)

with open('sdk/checkout-ui/src/test/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutViewModelTest.kt', 'w') as f:
    f.write(content)

with open('sdk/checkout-ui/src/androidTest/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutScreenTest.kt', 'r') as f:
    content2 = f.read()

content2 = content2.replace(
    'val checkoutSdk = mockk<CheckoutSdk>()',
    'val checkoutSdk = mockk<CheckoutSdk>(relaxed = true)'
)

with open('sdk/checkout-ui/src/androidTest/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutScreenTest.kt', 'w') as f:
    f.write(content2)
