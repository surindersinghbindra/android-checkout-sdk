with open('sdk/checkout-ui/src/androidTest/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutScreenTest.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'coEvery { checkoutSdk.executeCheckout(any(), any(), any()) } returns true',
    'coEvery { checkoutSdk.executeCheckout(any(), any(), any(), any()) } returns true'
)

with open('sdk/checkout-ui/src/androidTest/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutScreenTest.kt', 'w') as f:
    f.write(content)
