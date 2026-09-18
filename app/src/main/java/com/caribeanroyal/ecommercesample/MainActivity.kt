package com.caribeanroyal.ecommercesample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.caribeanroyal.ecommercesample.designsystem.theme.BrandPrimary
import com.caribeanroyal.ecommercesample.designsystem.theme.BrandSecondary
import com.caribeanroyal.ecommercesample.designsystem.theme.ECommerceTheme
import com.caribeanroyal.ecommercesample.navigation.AppNavigation
import com.caribeanroyal.ecommercesample.sdk.checkout.core.CheckoutSdk
import com.caribeanroyal.ecommercesample.sdk.checkout.ui.CheckoutThemeConfig

import com.caribeanroyal.ecommercesample.sdk.checkout.core.PaymentProcessorType

import com.caribeanroyal.ecommercesample.di.DaggerAppComponent

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val appComponent = DaggerAppComponent.factory().create(applicationContext)
        val getCruiseItineraryUseCase = appComponent.getCruiseItineraryUseCase()
        
        // Demonstrated Aggregator Pattern: SDK handles the processors internally!
        val checkoutSdk = CheckoutSdk.Builder()
            .enableProcessors(listOf(PaymentProcessorType.ADYEN, PaymentProcessorType.STRIPE))
            .setEnvironment("staging")
            .build()
            
        // 2. Build the dynamic runtime white-label theme configuration
        // We read the BrandPrimary and BrandSecondary from the currently running flavor variant!
        val sdkThemeConfig = CheckoutThemeConfig(
            primaryColor = BrandPrimary,
            secondaryColor = BrandSecondary,
            buttonCornerRadiusDp = 12
        )

        setContent {
            ECommerceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        checkoutSdk = checkoutSdk,
                        checkoutThemeConfig = sdkThemeConfig,
                        viewModelStoreOwner = this,
                        getCruiseItineraryUseCase = getCruiseItineraryUseCase
                    )
                }
            }
        }
    }
}
