package com.caribeanroyal.ecommercesample.sdk.checkout.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CheckoutScreen(
    amount: Double,
    currency: String,
    viewModel: CheckoutViewModel,
    themeConfig: CheckoutThemeConfig,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    
    var expanded by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    var selectedProcessor by androidx.compose.runtime.remember(state.availableProcessors) { 
        androidx.compose.runtime.mutableStateOf(state.availableProcessors.firstOrNull()) 
    }

    // Apply the dynamic SDK Theme
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = themeConfig.primaryColor,
            secondary = themeConfig.secondaryColor
        )
    ) {
        Surface(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onNavigateBack,
                    shape = RoundedCornerShape(themeConfig.buttonCornerRadiusDp.dp)
                ) {
                    Text("Back")
                }
                
                Spacer(modifier = Modifier.height(32.dp))

                when {
                    state.isLoading -> {
                        CircularProgressIndicator(color = themeConfig.primaryColor)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Processing Payment...", color = themeConfig.primaryColor)
                    }
                    state.isSuccess -> {
                        Text("Payment Successful!", color = Color.Green)
                    }
                    state.errorMessage != null -> {
                        Text("Error: ${state.errorMessage}", color = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.handleIntent(CheckoutIntent.RetryPayment) },
                            shape = RoundedCornerShape(themeConfig.buttonCornerRadiusDp.dp)
                        ) {
                            Text("Retry")
                        }
                    }
                    else -> {
                        Text("Total: $amount $currency", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        if (selectedProcessor != null) {
                            Box {
                                OutlinedButton(onClick = { expanded = true }) {
                                    Text("Payment Method: ${selectedProcessor?.displayName}")
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    state.availableProcessors.forEach { processor ->
                                        DropdownMenuItem(
                                            text = { Text(processor.displayName) },
                                            onClick = {
                                                selectedProcessor = processor
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        } else {
                            Text("No payment methods available", color = Color.Red)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { 
                                selectedProcessor?.let {
                                    viewModel.handleIntent(CheckoutIntent.SubmitPayment(amount, currency, it))
                                }
                            },
                            enabled = selectedProcessor != null,
                            shape = RoundedCornerShape(themeConfig.buttonCornerRadiusDp.dp)
                        ) {
                            Text("Pay Now")
                        }
                    }
                }
            }
        }
    }
}
