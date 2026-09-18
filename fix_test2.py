with open('sdk/checkout-ui/src/test/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutViewModelTest.kt', 'r') as f:
    content = f.read()

content = content.replace('coEvery { checkoutSdk.executeCheckout(100.0, "USD", PaymentProcessorType.STRIPE) } returns true', 
                          'coEvery { checkoutSdk.executeCheckout(100.0, "USD", PaymentProcessorType.STRIPE) } returns true\n        coEvery { checkoutSdk.submitPayment(100.0, "USD", PaymentProcessorType.STRIPE) } returns true')

with open('sdk/checkout-ui/src/test/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutViewModelTest.kt', 'w') as f:
    f.write(content)

with open('sdk/checkout-ui/src/androidTest/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutScreenTest.kt', 'r') as f:
    content2 = f.read()

content2 = content2.replace('coEvery { checkoutSdk.executeCheckout(any(), any(), any()) } returns true', 
                            'coEvery { checkoutSdk.executeCheckout(any(), any(), any()) } returns true\n        coEvery { checkoutSdk.submitPayment(any(), any(), any()) } returns true')

with open('sdk/checkout-ui/src/androidTest/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutScreenTest.kt', 'w') as f:
    f.write(content2)
