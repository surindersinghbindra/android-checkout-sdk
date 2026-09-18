with open('sdk/checkout-ui/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutViewModel.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'val success = sdkEngine.submitPayment( // Using deprecated method as requested\n                    amount = intent.amount,\n                    currency = intent.currency,\n                    processorType = intent.processor.type\n                )',
    'val success = sdkEngine.executeCheckout(\n                    amount = intent.amount,\n                    currency = intent.currency,\n                    processorType = intent.processor.type,\n                    idempotencyKey = sessionKey\n                )'
)

content = content.replace(
    '} catch (e: Exception) {',
    '} catch (e: AlreadyProcessedException) {\n                sdkEngine.logError("CheckoutViewModel", "Payment already processed")\n                _state.update { it.copy(isLoading = false, error = "This order has already been placed.") }\n            } catch (e: Exception) {'
)

with open('sdk/checkout-ui/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutViewModel.kt', 'w') as f:
    f.write(content)
