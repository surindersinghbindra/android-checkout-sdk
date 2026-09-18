import re

with open('sdk/checkout-ui/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutScreen.kt', 'r') as f:
    content = f.read()

# 1. Add CheckoutStepType and rememberCheckoutSteps
types_code = """
enum class CheckoutStepType(val title: String) {
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

content = content.replace("package com.caribeanroyal.ecommercesample.sdk.checkout.ui\n", "package com.caribeanroyal.ecommercesample.sdk.checkout.ui\n" + types_code)

# 2. Update CheckoutScreen signature
content = content.replace(
    "    themeConfig: CheckoutThemeConfig,\n    onNavigateBack: () -> Unit,",
    "    themeConfig: CheckoutThemeConfig,\n    stepsConfig: CheckoutStepsConfig = CheckoutStepsConfig(),\n    onNavigateBack: () -> Unit,"
)

# 3. Add steps logic inside CheckoutScreen
content = content.replace(
    "val state by viewModel.state.collectAsState()",
    "val state by viewModel.state.collectAsState()\n    val steps = rememberCheckoutSteps(stepsConfig)\n    val maxStep = steps.size - 1"
)

# 4. Update Next button
content = content.replace(
    "viewModel.handleIntent(CheckoutIntent.NextStep)",
    "viewModel.handleIntent(CheckoutIntent.NextStep(maxStep))"
)

# 5. Update VisualStepper call
content = content.replace(
    "VisualStepper(currentStep = state.currentStep, totalSteps = 6, themeConfig = themeConfig)",
    "VisualStepper(currentStep = state.currentStep, steps = steps, themeConfig = themeConfig)"
)

# 6. Update when block
when_block_old = """                        when (targetStep) {
                            0 -> PartySizeStep(state, viewModel)
                            1 -> RoomSelectionStep(state, viewModel, themeConfig)
                            2 -> AddonsStep(state, viewModel)
                            3 -> ReviewStep(state, orderTitle, orderDescription)
                            4 -> GuestInfoStep(state, viewModel)
                            5 -> PayStep(state, orderTitle, orderDescription, themeConfig, onNavigateBack)
                        }"""
when_block_new = """                        when (steps.getOrNull(targetStep)) {
                            CheckoutStepType.PARTY_SIZE -> PartySizeStep(state, viewModel)
                            CheckoutStepType.ROOM_SELECTION -> RoomSelectionStep(state, viewModel, themeConfig)
                            CheckoutStepType.EXTRAS -> AddonsStep(state, viewModel)
                            CheckoutStepType.REVIEW -> ReviewStep(state, orderTitle, orderDescription)
                            CheckoutStepType.GUEST_INFO -> GuestInfoStep(state, viewModel)
                            CheckoutStepType.PAY -> PayStep(state, orderTitle, orderDescription, themeConfig, onNavigateBack)
                            null -> {}
                        }"""
content = content.replace(when_block_old, when_block_new)

# 7. Update VisualStepper definition
stepper_old = """@Composable
fun VisualStepper(currentStep: Int, totalSteps: Int, themeConfig: CheckoutThemeConfig) {
    val stepTitles = listOf("Party", "Room", "Extras", "Review", "Guest", "Pay")
    Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
        for (i in 0 until totalSteps) {"""

stepper_new = """@Composable
fun VisualStepper(currentStep: Int, steps: List<CheckoutStepType>, themeConfig: CheckoutThemeConfig) {
    val totalSteps = steps.size
    Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
        for (i in 0 until totalSteps) {"""
content = content.replace(stepper_old, stepper_new)

content = content.replace(
    "text = stepTitles.getOrElse(i) { \"\" },",
    "text = steps.getOrNull(i)?.title ?: \"\","
)

with open('sdk/checkout-ui/src/main/java/com/caribeanroyal/ecommercesample/sdk/checkout/ui/CheckoutScreen.kt', 'w') as f:
    f.write(content)
