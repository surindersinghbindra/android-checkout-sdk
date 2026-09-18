package com.caribeanroyal.headless.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.startup.AppInitializer
import androidx.compose.ui.platform.LocalContext
import com.caribeanroyal.ecommercesample.sdk.checkout.core.db.CheckoutSdkInitializer
import com.caribeanroyal.ecommercesample.sdk.checkout.core.CheckoutSdk
import com.caribeanroyal.headless.ui.CustomHeadlessCheckoutScreen

fun NavGraphBuilder.checkoutGraph(
    navController: NavController,
    sdkEngine: CheckoutSdk
) {
    composable("checkout/{price}/{currency}/{title}/{desc}") { backStackEntry ->
        val priceStr = backStackEntry.arguments?.getString("price") ?: "0.0"
        val currency = backStackEntry.arguments?.getString("currency") ?: "USD"
        val title = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("title") ?: "", "UTF-8")
        val desc = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("desc") ?: "", "UTF-8")
        val price = priceStr.toDoubleOrNull() ?: 0.0

        // Manually trigger the initializer only when the checkout feature is opened!
        AppInitializer.getInstance(LocalContext.current).initializeComponent(CheckoutSdkInitializer::class.java)
        
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
