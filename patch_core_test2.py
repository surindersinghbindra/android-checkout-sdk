with open('sdk/checkout-core/src/test/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdkTest.kt', 'r') as f:
    content = f.read()

content = content.replace('import io.mockk.every\n', 'import io.mockk.every\nimport io.mockk.mockkStatic\nimport androidx.startup.AppInitializer\nimport com.caribeanroyal.ecommercesample.sdk.checkout.core.db.CheckoutDatabase\nimport com.caribeanroyal.ecommercesample.sdk.checkout.core.db.IdempotencyDao\n')

mock_db = """        mockkStatic(AppInitializer::class)
        val mockInitializer = mockk<AppInitializer>(relaxed = true)
        every { AppInitializer.getInstance(any()) } returns mockInitializer
        val mockDb = mockk<CheckoutDatabase>(relaxed = true)
        val mockDao = mockk<IdempotencyDao>(relaxed = true)
        every { mockDb.idempotencyDao() } returns mockDao
        every { mockInitializer.initializeComponent(any<Class<*>>()) } returns mockDb
"""

content = content.replace('val mockProcessor = mockk<PaymentProcessor>()', mock_db + '\n        val mockProcessor = mockk<PaymentProcessor>()')

with open('sdk/checkout-core/src/test/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdkTest.kt', 'w') as f:
    f.write(content)
