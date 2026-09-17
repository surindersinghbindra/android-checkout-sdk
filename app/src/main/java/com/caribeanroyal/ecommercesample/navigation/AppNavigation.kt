package com.caribeanroyal.ecommercesample.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.caribeanroyal.ecommercesample.feature.booking.ui.BookingScreen
import com.caribeanroyal.ecommercesample.sdk.checkout.core.CheckoutSdk
import com.caribeanroyal.ecommercesample.sdk.checkout.ui.CheckoutScreen
import com.caribeanroyal.ecommercesample.sdk.checkout.ui.CheckoutThemeConfig
import com.caribeanroyal.ecommercesample.sdk.checkout.ui.CheckoutViewModel
import com.caribeanroyal.ecommercesample.sdk.checkout.ui.CheckoutViewModelFactory

@Composable
fun AppNavigation(
    checkoutSdk: CheckoutSdk,
    checkoutThemeConfig: CheckoutThemeConfig,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "booking",
        modifier = modifier
    ) {
        composable("booking") {
            BookingScreen(
                onNavigateToCheckout = {
                    navController.navigate("checkout")
                }
            )
        }
        
        composable("checkout") {
            // Instantiate the SDK's ViewModel
            val factory = CheckoutViewModelFactory(checkoutSdk)
            val checkoutViewModel = ViewModelProvider(viewModelStoreOwner, factory)[CheckoutViewModel::class.java]
            
            CheckoutScreen(
                viewModel = checkoutViewModel,
                themeConfig = checkoutThemeConfig,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
