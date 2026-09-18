with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/PaymentProcessor.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'package com.caribeanroyal.ecommercesample.sdk.checkout.core',
    'package com.caribeanroyal.ecommercesample.sdk.checkout.core\n\nimport androidx.annotation.Keep'
)

content = content.replace(
    'enum class PaymentProcessorType',
    '@Keep\nenum class PaymentProcessorType'
)

content = content.replace(
    'interface PaymentProcessor',
    '@Keep\ninterface PaymentProcessor'
)

with open('sdk/checkout-core/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/core/PaymentProcessor.kt', 'w') as f:
    f.write(content)
