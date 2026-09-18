package com.caribeanroyal.headless

import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import com.caribeanroyal.headless.navigation.ECommerceHeadlessApp

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
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        ECommerceHeadlessApp(
                            sdkEngine = sdkEngine,
                            viewModelStoreOwner = this@MainActivity,
                            bookingFactory = bookingFactory
                        )
                    }
                }
            }
        }
    }
}

