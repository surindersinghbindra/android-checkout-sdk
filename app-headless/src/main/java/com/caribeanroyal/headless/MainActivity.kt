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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.caribeanroyal.ecommercesample.core.network.repository.NetworkCruiseRepositoryImpl
import com.caribeanroyal.ecommercesample.core.domain.usecase.SearchCruisesUseCase
import com.caribeanroyal.ecommercesample.core.network.api.CruiseApiService
import com.caribeanroyal.ecommercesample.core.network.interceptor.MockCruiseInterceptor
import com.caribeanroyal.ecommercesample.designsystem.theme.ECommerceTheme
import com.caribeanroyal.ecommercesample.feature.booking.ui.BookingScreen
import com.caribeanroyal.ecommercesample.feature.booking.ui.CruiseDetailScreen
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModel
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModelFactory
import com.caribeanroyal.ecommercesample.sdk.checkout.core.CheckoutAnalytics
import android.util.Log
import com.caribeanroyal.ecommercesample.sdk.checkout.core.CheckoutSdk
import com.caribeanroyal.ecommercesample.sdk.checkout.core.PaymentProcessorType
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(MockCruiseInterceptor())
            .build()
            
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.royalcaribbean.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            
        val apiService = retrofit.create(CruiseApiService::class.java)
        val repository = NetworkCruiseRepositoryImpl(apiService)
        val searchCruisesUseCase = SearchCruisesUseCase(repository)
        val bookingFactory = BookingViewModelFactory(searchCruisesUseCase)

        val analyticsTracker = object : CheckoutAnalytics {
            override fun logEvent(eventName: String, params: Map<String, Any>) {
                Log.d("HeadlessAnalytics", "EVENT: $eventName")
            }
            override fun logError(throwable: Throwable, message: String) {
                Log.e("HeadlessAnalytics", "ERROR: $message", throwable)
            }
        }
        
        val sdkEngine = CheckoutSdk.Builder()
            .enableProcessors(listOf(PaymentProcessorType.ADYEN, PaymentProcessorType.STRIPE, PaymentProcessorType.FAIL_SIMULATOR))
            .setEnvironment("staging")
            .setDebuggable(true)
            .setAnalytics(analyticsTracker)
            .build()

        setContent {
            ECommerceTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    
                    NavHost(navController = navController, startDestination = "booking") {
                        composable("booking") { backStackEntry ->
                            val bookingViewModel = ViewModelProvider(this@MainActivity, bookingFactory)[BookingViewModel::class.java]
                            
                            BookingScreen(
                                viewModel = bookingViewModel,
                                onNavigateToCruiseDetail = { packageCode ->
                                    navController.navigate("cruiseDetail/$packageCode")
                                }
                            )
                        }
                        
                        composable("cruiseDetail/{packageCode}") { backStackEntry ->
                            val packageCode = backStackEntry.arguments?.getString("packageCode") ?: ""
                            val bookingViewModel = ViewModelProvider(this@MainActivity, bookingFactory)[BookingViewModel::class.java]

                            CruiseDetailScreen(
                                packageCode = packageCode,
                                viewModel = bookingViewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToCheckout = { price, currency, title, desc ->
                                    navController.navigate("checkout/$price/$currency/${java.net.URLEncoder.encode(title, "UTF-8")}/${java.net.URLEncoder.encode(desc, "UTF-8")}")
                                }
                            )
                        }

                        composable("checkout/{price}/{currency}/{title}/{desc}") { backStackEntry ->
                            val priceStr = backStackEntry.arguments?.getString("price") ?: "0.0"
                            val currency = backStackEntry.arguments?.getString("currency") ?: "USD"
                            val title = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("title") ?: "", "UTF-8")
                            val desc = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("desc") ?: "", "UTF-8")
                            val price = priceStr.toDoubleOrNull() ?: 0.0
                            
                            CustomHeadlessCheckoutScreen(
                                sdkEngine = sdkEngine,
                                amount = price,
                                currency = currency,
                                orderTitle = title,
                                orderDescription = desc,
                                onCheckoutSuccess = { navController.popBackStack("booking", inclusive = false) },
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

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
