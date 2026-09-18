package com.caribeanroyal.headless.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.caribeanroyal.ecommercesample.sdk.checkout.core.CheckoutSdk
import kotlinx.coroutines.launch

@Composable
fun CustomHeadlessCheckoutScreen(
    sdkEngine: CheckoutSdk, 
    amount: Double,
    currency: String,
    orderTitle: String,
    orderDescription: String,
    onBack: () -> Unit,
    onCheckoutSuccess: () -> Unit = onBack
) {
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
        Text(
            text = "Custom Headless Checkout",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(orderTitle, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(orderDescription, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Amount Due: $currency $amount", style = MaterialTheme.typography.titleLarge)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(text = "Status: $status", color = Color.DarkGray)
        Spacer(modifier = Modifier.height(24.dp))
        
        if (isProcessing) {
            CircularProgressIndicator()
        } else if (status == "Payment Successful!") {
            Text("Payment Successful!", color = Color(0xFF4CAF50), style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onCheckoutSuccess) {
                Text("Explore More")
            }
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
                            val result = sdkEngine.executeCheckout(amount, currency, processor)
                            isProcessing = false
                            status = if (result) "Payment Successful!" else "Payment Failed."
                        }
                    }
                },
                enabled = selectedProcessor != null,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Pay $currency $amount")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextButton(onClick = onCheckoutSuccess) {
                Text("Go Back")
            }
        }
    }
}
