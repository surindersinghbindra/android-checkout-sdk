with open('README.md', 'r') as f:
    content = f.read()

idempotency_docs = """
### Idempotency & Double-Booking Prevention
The SDK engine now natively guards against double-charging via Idempotency Keys. 
1. The host application (e.g. `CheckoutViewModel` in `checkout-ui` or a custom headless ViewModel) generates a single unique session key per checkout flow (e.g., `UUID`).
2. This key is passed down into `executeCheckout(amount, currency, processor, idempotencyKey)`.
3. If the user accidentally double-taps or a network retry happens, the SDK core will intercept the identical key, prevent the charge, and throw an `AlreadyProcessedException`.
4. The `CheckoutViewModel` catches this and smoothly updates the UI to show: *"This order has already been placed."*
"""

# Insert before "### Deprecations" in README.md
content = content.replace('### Deprecations\n', idempotency_docs + '\n### Deprecations\n')

with open('README.md', 'w') as f:
    f.write(content)
