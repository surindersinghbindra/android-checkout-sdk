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
import com.caribeanroyal.ecommercesample.sdk.checkout.core.PaymentProcessor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Custom Payment Processor implemented by the Headless App Team
class HeadlessPaymentProcessor : PaymentProcessor {
    override suspend fun processPayment(amount: Double, currency: String): Boolean {
        delay(1500)
        return true
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Initialize ONLY the Core SDK
        val sdkEngine = CheckoutSdk.Builder()
            .setPaymentProcessor(HeadlessPaymentProcessor())
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
            Button(
                onClick = {
                    coroutineScope.launch {
                        isProcessing = true
                        status = "Processing via Core SDK..."
                        val result = sdkEngine.executeCheckout(299.99, "USD")
                        isProcessing = false
                        status = if (result) "Payment Successful!" else "Payment Failed."
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Pay $299.99")
            }
        }
    }
}
