with open('sdk/checkout-core/src/test/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdkTest.kt', 'r') as f:
    content = f.read()

content = content.replace('any()', 'io.mockk.eq(com.caribeanroyal.ecommercesample.sdk.checkout.core.db.CheckoutSdkInitializer::class.java)')

# we also need to fix `any<Class<*>>()` or just ensure `any()` was correctly replaced.
# Wait, I previously replaced 'any<Class<*>>()' with 'any()'.
# Let's just do a direct replacement on the mock line.
content = content.replace(
    'every { mockInitializer.initializeComponent(io.mockk.eq(com.caribeanroyal.ecommercesample.sdk.checkout.core.db.CheckoutSdkInitializer::class.java)) } returns mockDb',
    'every { mockInitializer.initializeComponent(CheckoutSdkInitializer::class.java) } returns mockDb'
)
content = content.replace(
    'every { AppInitializer.getInstance(io.mockk.eq(com.caribeanroyal.ecommercesample.sdk.checkout.core.db.CheckoutSdkInitializer::class.java)) } returns mockInitializer',
    'every { AppInitializer.getInstance(any()) } returns mockInitializer'
)

with open('sdk/checkout-core/src/test/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdkTest.kt', 'w') as f:
    f.write(content)
