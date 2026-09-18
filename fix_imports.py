with open('sdk/checkout-ui/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutScreen.kt', 'r') as f:
    content = f.read()

types_code = """enum class CheckoutStepType(val title: String) {
    PARTY_SIZE("Party"),
    ROOM_SELECTION("Room"),
    EXTRAS("Extras"),
    REVIEW("Review"),
    GUEST_INFO("Guest"),
    PAY("Pay")
}

@Composable
fun rememberCheckoutSteps(config: CheckoutStepsConfig): List<CheckoutStepType> {
    return remember(config) {
        val steps = mutableListOf<CheckoutStepType>()
        if (config.showPartySize) steps.add(CheckoutStepType.PARTY_SIZE)
        if (config.showRoomSelection) steps.add(CheckoutStepType.ROOM_SELECTION)
        if (config.showExtras) steps.add(CheckoutStepType.EXTRAS)
        steps.add(CheckoutStepType.REVIEW)
        if (config.showGuestInfo) steps.add(CheckoutStepType.GUEST_INFO)
        steps.add(CheckoutStepType.PAY)
        steps
    }
}
"""

content = content.replace(types_code, "")
content = content.replace("package com.caribeanroyal.ecommercesample.sdk.checkout.ui\n\n\n\n", "package com.caribeanroyal.ecommercesample.sdk.checkout.ui\n\n")

# Find the end of imports
lines = content.split('\n')
last_import_idx = 0
for i, line in enumerate(lines):
    if line.startswith("import "):
        last_import_idx = i

lines.insert(last_import_idx + 1, "\n" + types_code)
content = '\n'.join(lines)

with open('sdk/checkout-ui/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutScreen.kt', 'w') as f:
    f.write(content)
