with open('sdk/checkout-core/src/test/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdkTest.kt', 'r') as f:
    content = f.read()

content = content.replace('import org.junit.Test', 'import org.junit.Test\nimport android.content.Context\nimport io.mockk.every')

# Fix Legacy Test
content = content.replace(
    'val sdk = CheckoutSdk(mockProcessor)',
    'val context = mockk<Context>(relaxed = true)\n        val sdk = CheckoutSdk.Builder(context).setPaymentProcessor(mockProcessor).build()'
)
content = content.replace('@Suppress("DEPRECATION")\n        val context = mockk<Context>', 'val context = mockk<Context>')

# Fix Builder Test
content = content.replace(
    'val sdk = CheckoutSdk.Builder()',
    'val context = mockk<Context>(relaxed = true)\n        val sdk = CheckoutSdk.Builder(context)'
)

with open('sdk/checkout-core/src/test/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdkTest.kt', 'w') as f:
    f.write(content)
