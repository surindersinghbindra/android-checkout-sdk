@file:OptIn(ExperimentalMaterial3Api::class)
package com.caribeanroyal.ecommercesample.sdk.checkout.ui

import androidx.annotation.Keep

import androidx.compose.foundation.layout.*
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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


@Keep
@Composable
fun CheckoutScreen(
    amount: Double,
    currency: String,
    orderTitle: String = "Order Summary",
    orderDescription: String = "",
    viewModel: CheckoutViewModel,
    themeConfig: CheckoutThemeConfig,
    stepsConfig: CheckoutStepsConfig = CheckoutStepsConfig(),
    onNavigateBack: () -> Unit,
    onCheckoutSuccess: () -> Unit = onNavigateBack,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val steps = rememberCheckoutSteps(stepsConfig)
    val maxStep = steps.size - 1
    val dynamicTotal = viewModel.calculateDynamicTotal(amount)

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = themeConfig.primaryColor,
            secondary = themeConfig.secondaryColor
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(orderTitle) },
                    navigationIcon = {
                        Button(onClick = {
                            if (state.currentStep > 0) viewModel.handleIntent(CheckoutIntent.PreviousStep)
                            else onNavigateBack()
                        }, modifier = Modifier.padding(horizontal = 8.dp)) {
                            Text(if (state.currentStep > 0) "Back" else "Cancel")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = themeConfig.secondaryColor.copy(alpha = 0.1f))
                )
            },
            bottomBar = {
                Surface(shadowElevation = 8.dp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total due today")
                            Text("$currency $dynamicTotal", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = themeConfig.primaryColor)
                        }
                        
                        if (state.currentStep < 5) {
                            Button(onClick = { viewModel.handleIntent(CheckoutIntent.NextStep(maxStep)) }) {
                                Text("Next Step")
                            }
                        } else {
                            var expanded by remember { mutableStateOf(false) }
                            var selectedProcessor by remember(state.availableProcessors) { mutableStateOf(state.availableProcessors.firstOrNull()) }
                            
                            Row {
                                Box(modifier = Modifier.padding(end = 8.dp)) {
                                    OutlinedButton(onClick = { expanded = true }) {
                                        Text(selectedProcessor?.type?.displayName ?: "Method")
                                    }
                                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                        state.availableProcessors.forEach { processor ->
                                            DropdownMenuItem(
                                                text = { Text(processor.type.displayName) },
                                                onClick = {
                                                    selectedProcessor = processor
                                                    expanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                                Button(
                                    onClick = { 
                                        selectedProcessor?.let {
                                            viewModel.handleIntent(CheckoutIntent.SubmitPayment(dynamicTotal, currency, it))
                                        }
                                    },
                                    enabled = selectedProcessor != null && !state.isLoading && !state.isSuccess
                                ) {
                                    Text("Pay Now")
                                }
                            }
                        }
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Stepper Header
                VisualStepper(currentStep = state.currentStep, steps = steps, themeConfig = themeConfig)
                Spacer(modifier = Modifier.height(24.dp))
                
                AnimatedContent(
                    targetState = state.currentStep,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                                slideOutHorizontally { width -> -width } + fadeOut()
                            )
                        } else {
                            (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                                slideOutHorizontally { width -> width } + fadeOut()
                            )
                        }
                    },
                    label = "checkout_stepper_animation"
                ) { targetStep ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        when (steps.getOrNull(targetStep)) {
                            CheckoutStepType.PARTY_SIZE -> PartySizeStep(state, viewModel)
                            CheckoutStepType.ROOM_SELECTION -> RoomSelectionStep(state, viewModel, themeConfig)
                            CheckoutStepType.EXTRAS -> AddonsStep(state, viewModel)
                            CheckoutStepType.REVIEW -> ReviewStep(state, orderTitle, orderDescription)
                            CheckoutStepType.GUEST_INFO -> GuestInfoStep(state, viewModel)
                            CheckoutStepType.PAY -> PayStep(state, orderTitle, orderDescription, themeConfig, onCheckoutSuccess)
                            null -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PartySizeStep(state: CheckoutState, viewModel: CheckoutViewModel) {
    Text("How many people are travelling?", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(16.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = { viewModel.handleIntent(CheckoutIntent.UpdatePartySize(maxOf(1, state.partySize - 1))) }) { Text("-") }
        Text("${state.partySize}", modifier = Modifier.padding(horizontal = 24.dp), style = MaterialTheme.typography.headlineMedium)
        Button(onClick = { viewModel.handleIntent(CheckoutIntent.UpdatePartySize(state.partySize + 1)) }) { Text("+") }
    }
}

@Composable
fun RoomSelectionStep(state: CheckoutState, viewModel: CheckoutViewModel, themeConfig: CheckoutThemeConfig) {
    Text("Select your room", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(16.dp))
    state.roomPrices.forEach { (room, extraPrice) ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (state.roomType == room) themeConfig.primaryColor.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant
            ),
            onClick = { viewModel.handleIntent(CheckoutIntent.SelectRoom(room)) }
        ) {
            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(room, fontWeight = FontWeight.Bold)
                Text("+$extraPrice")
            }
        }
    }
}

@Composable
fun AddonsStep(state: CheckoutState, viewModel: CheckoutViewModel) {
    Text("Customize your cruise", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(16.dp))
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text("Drink Package", fontWeight = FontWeight.Bold)
            Text("120.0 per guest")
        }
        Switch(checked = state.includeDrinkPackage, onCheckedChange = { viewModel.handleIntent(CheckoutIntent.ToggleDrinkPackage(it)) })
    }
    Spacer(modifier = Modifier.height(16.dp))
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text("WiFi", fontWeight = FontWeight.Bold)
            Text("50.0 per guest")
        }
        Switch(checked = state.includeWiFi, onCheckedChange = { viewModel.handleIntent(CheckoutIntent.ToggleWiFi(it)) })
    }
}

@Composable
fun ReviewStep(state: CheckoutState, orderTitle: String, orderDescription: String) {
    Text("Review your choices", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(16.dp))
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(orderTitle, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text(orderDescription, style = MaterialTheme.typography.bodyMedium)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text("Guests: ${state.partySize}")
            Text("Room: ${state.roomType}")
            if (state.includeDrinkPackage) Text("Add-on: Drink Package")
            if (state.includeWiFi) Text("Add-on: WiFi")
        }
    }
}

@Composable
fun GuestInfoStep(state: CheckoutState, viewModel: CheckoutViewModel) {
    Text("Primary Guest Information", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(16.dp))
    OutlinedTextField(
        value = state.guestFirstName,
        onValueChange = { viewModel.handleIntent(CheckoutIntent.UpdateGuestInfo(it, state.guestLastName)) },
        label = { Text("First legal name") },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = state.guestLastName,
        onValueChange = { viewModel.handleIntent(CheckoutIntent.UpdateGuestInfo(state.guestFirstName, it)) },
        label = { Text("Last legal name") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PayStep(state: CheckoutState, orderTitle: String, orderDescription: String, themeConfig: CheckoutThemeConfig, onCheckoutComplete: () -> Unit) {
    LaunchedEffect(state.isSuccess, themeConfig.showExploreMoreButton) {
        if (state.isSuccess && !themeConfig.showExploreMoreButton) {
            delay(1000L)
            onCheckoutComplete()
        }
    }

    if (state.isSuccess) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text("Payment Successful!", color = Color(0xFF4CAF50), style = MaterialTheme.typography.headlineMedium)
            
            if (themeConfig.showExploreMoreButton) {
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onCheckoutComplete) {
                    Text("Explore More")
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Returning to booking...", color = Color.Gray)
            }
        }
    } else if (state.isLoading) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            CircularProgressIndicator(color = themeConfig.primaryColor)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Processing Payment...")
        }
    } else {
        Text("Ready to complete booking.", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(orderTitle, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(orderDescription, style = MaterialTheme.typography.bodyMedium)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Primary Guest: ${state.guestFirstName} ${state.guestLastName}")
                Text("Guests: ${state.partySize}")
                Text("Room: ${state.roomType}")
                if (state.includeDrinkPackage) Text("Add-on: Drink Package")
                if (state.includeWiFi) Text("Add-on: WiFi")
            }
        }
        
        state.error?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Error: $it", color = Color.Red)
        }
    }
}

@Composable
fun VisualStepper(currentStep: Int, steps: List<CheckoutStepType>, themeConfig: CheckoutThemeConfig) {
    val totalSteps = steps.size
    Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
        for (i in 0 until totalSteps) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(48.dp)) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = if (i <= currentStep) themeConfig.primaryColor else Color.LightGray,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "${i + 1}", color = Color.White, style = MaterialTheme.typography.labelSmall)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = steps.getOrNull(i)?.title ?: "", 
                    style = MaterialTheme.typography.labelSmall, 
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (i <= currentStep) themeConfig.primaryColor else Color.Gray
                )
            }
            if (i < totalSteps - 1) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 11.dp) // Half of 24.dp to center with circle
                        .height(2.dp)
                        .background(if (i < currentStep) themeConfig.primaryColor else Color.LightGray)
                )
            }
        }
    }
}
