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
import com.caribeanroyal.ecommercesample.sdk.checkout.core.PaymentProcessor
import com.caribeanroyal.ecommercesample.sdk.checkout.ui.CheckoutThemeConfig
import kotlinx.coroutines.delay

// A dummy implementation of the PaymentProcessor strategy for the host app.
class StripePaymentProcessor : PaymentProcessor {
    override suspend fun processPayment(amount: Double, currency: String): Boolean {
        delay(1000)
        return amount > 0
    }
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Initialize the SDK using the new Builder
        val checkoutSdk = CheckoutSdk.Builder()
            .setPaymentProcessor(StripePaymentProcessor())
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
                        viewModelStoreOwner = this
                    )
                }
            }
        }
    }
}
