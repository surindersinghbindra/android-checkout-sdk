package com.caribeanroyal.headless

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.caribeanroyal.ecommercesample.sdk.checkout.core.CheckoutSdk
import com.caribeanroyal.ecommercesample.sdk.checkout.core.PaymentProcessorType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Initialize ONLY the Core SDK with built-in processors
        val sdkEngine = CheckoutSdk.Builder()
            .enableProcessors(listOf(PaymentProcessorType.ADYEN, PaymentProcessorType.STRIPE))
            .setEnvironment("headless-production")
            .build()

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CustomHeadlessCheckoutScreen(sdkEngine)
                }
            }
        }
    }
}

@Composable
fun CustomHeadlessCheckoutScreen(sdkEngine: CheckoutSdk) {
    val coroutineScope = rememberCoroutineScope()
    var status by remember { mutableStateOf("Ready to pay") }
    var isProcessing by remember { mutableStateOf(false) }
    
    var expanded by remember { mutableStateOf(false) }
    val processors = sdkEngine.enabledProcessors.map { it.type }
    var selectedProcessor by remember { mutableStateOf(processors.firstOrNull()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Notice we are NOT using CheckoutThemeConfig or CheckoutViewModel from :sdk:checkout-ui
        // We are building a completely custom UI structure!
        Text(
            text = "Custom Headless Checkout",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(text = "Status: $status", color = Color.DarkGray)
        Spacer(modifier = Modifier.height(24.dp))
        
        if (isProcessing) {
            CircularProgressIndicator()
        } else {
            if (selectedProcessor != null) {
                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text("Processor: ${selectedProcessor?.displayName}")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        processors.forEach { processor ->
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
            }
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    selectedProcessor?.let { processor ->
                        coroutineScope.launch {
                            isProcessing = true
                            status = "Processing via Core SDK..."
                            val result = sdkEngine.executeCheckout(299.99, "USD", processor)
                            isProcessing = false
                            status = if (result) "Payment Successful!" else "Payment Failed."
                        }
                    }
                },
                enabled = selectedProcessor != null,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Pay $299.99")
            }
        }
    }
}
