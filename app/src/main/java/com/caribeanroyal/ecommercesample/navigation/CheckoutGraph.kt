package com.caribeanroyal.ecommercesample.navigation

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.startup.AppInitializer
import androidx.compose.ui.platform.LocalContext
import com.caribeanroyal.ecommercesample.sdk.checkout.core.db.CheckoutSdkInitializer
import androidx.navigation.toRoute
import com.caribeanroyal.ecommercesample.sdk.checkout.core.CheckoutSdk
import com.caribeanroyal.ecommercesample.sdk.checkout.ui.CheckoutScreen
import com.caribeanroyal.ecommercesample.sdk.checkout.ui.CheckoutStepsConfig
import com.caribeanroyal.ecommercesample.sdk.checkout.ui.CheckoutThemeConfig
import com.caribeanroyal.ecommercesample.sdk.checkout.ui.CheckoutViewModel
import com.caribeanroyal.ecommercesample.sdk.checkout.ui.CheckoutViewModelFactory

fun NavGraphBuilder.checkoutGraph(
    navController: NavController,
    checkoutSdk: CheckoutSdk,
    checkoutThemeConfig: CheckoutThemeConfig
) {
    composable<Screen.Checkout> { backStackEntry ->
        val checkoutRoute = backStackEntry.toRoute<Screen.Checkout>()

        val factory = CheckoutViewModelFactory(checkoutSdk)
        val checkoutViewModel =
            ViewModelProvider(backStackEntry, factory)[CheckoutViewModel::class.java]

        CheckoutScreen(
            amount = checkoutRoute.price,
            currency = checkoutRoute.currency,
            orderTitle = checkoutRoute.orderTitle,
            orderDescription = checkoutRoute.orderDescription,
            viewModel = checkoutViewModel,
            themeConfig = checkoutThemeConfig,
            stepsConfig = CheckoutStepsConfig(
                showExtras = true,
                showPartySize = true,
                showRoomSelection = true
            ),
            onCheckoutSuccess = {
                navController.popBackStack<Screen.Booking>(inclusive = false)
            },
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}
