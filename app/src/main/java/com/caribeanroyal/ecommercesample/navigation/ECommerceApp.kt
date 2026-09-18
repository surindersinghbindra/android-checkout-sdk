package com.caribeanroyal.ecommercesample.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.caribeanroyal.ecommercesample.sdk.checkout.core.CheckoutSdk
import com.caribeanroyal.ecommercesample.sdk.checkout.ui.CheckoutThemeConfig
import com.caribeanroyal.ecommercesample.ui.SplashScreen

@Composable
fun ECommerceApp(
    checkoutSdk: CheckoutSdk,
    checkoutThemeConfig: CheckoutThemeConfig,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash,
        modifier = modifier
    ) {
        composable<Screen.Splash> {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Booking) {
                        popUpTo<Screen.Splash> { inclusive = true }
                    }
                }
            )
        }

        bookingGraph(
            navController = navController
        )

        checkoutGraph(
            navController = navController,
            checkoutSdk = checkoutSdk,
            checkoutThemeConfig = checkoutThemeConfig
        )
    }
}
