with open('app-headless/src/main/java/com/caribeanroyal/headless/ui/CustomHeadlessCheckoutScreen.kt', 'r') as f:
    content = f.read()

content = content.replace('import androidx.compose.ui.unit.dp\n', 'import androidx.compose.ui.unit.dp\nimport java.util.UUID\nimport com.caribeanroyal.ecommercesample.sdk.checkout.core.AlreadyProcessedException\n')

content = content.replace(
    'var status by remember { mutableStateOf("Ready to pay") }',
    'var status by remember { mutableStateOf("Ready to pay") }\n    val sessionKey = remember { UUID.randomUUID().toString() }'
)

old_click = """                        coroutineScope.launch {
                            isProcessing = true
                            status = "Processing via Core SDK..."
                            val result = sdkEngine.executeCheckout(amount, currency, processor)
                            isProcessing = false
                            status = if (result) "Payment Successful!" else "Payment Failed."
                        }"""

new_click = """                        coroutineScope.launch {
                            isProcessing = true
                            status = "Processing via Core SDK..."
                            try {
                                val result = sdkEngine.executeCheckout(amount, currency, processor, sessionKey)
                                status = if (result) "Payment Successful!" else "Payment Failed."
                            } catch (e: AlreadyProcessedException) {
                                status = "This order has already been placed."
                            } catch (e: Exception) {
                                status = "Payment Error: ${e.message}"
                            }
                            isProcessing = false
                        }"""
content = content.replace(old_click, new_click)

with open('app-headless/src/main/java/com/caribeanroyal/headless/ui/CustomHeadlessCheckoutScreen.kt', 'w') as f:
    f.write(content)
