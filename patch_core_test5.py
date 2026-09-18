with open('sdk/checkout-core/src/test/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdkTest.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'import com.caribeanroyal.ecommercesample.sdk.checkout.core.db.IdempotencyDao\n',
    'import com.caribeanroyal.ecommercesample.sdk.checkout.core.db.IdempotencyDao\nimport com.caribeanroyal.ecommercesample.sdk.checkout.core.db.CheckoutSdkInitializer\n'
)

with open('sdk/checkout-core/src/test/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/CheckoutSdkTest.kt', 'w') as f:
    f.write(content)
