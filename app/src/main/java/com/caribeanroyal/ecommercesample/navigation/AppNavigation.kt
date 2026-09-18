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

import androidx.navigation.toRoute

import com.caribeanroyal.ecommercesample.core.domain.usecase.GetCruiseItineraryUseCase
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModel
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModelFactory

@Composable
fun AppNavigation(
    checkoutSdk: CheckoutSdk,
    checkoutThemeConfig: CheckoutThemeConfig,
    viewModelStoreOwner: ViewModelStoreOwner,
    getCruiseItineraryUseCase: GetCruiseItineraryUseCase,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Booking,
        modifier = modifier
    ) {
        composable<Screen.Booking> { backStackEntry ->
            val factory = BookingViewModelFactory(getCruiseItineraryUseCase)
            val bookingViewModel = ViewModelProvider(backStackEntry, factory)[BookingViewModel::class.java]
            
            BookingScreen(
                viewModel = bookingViewModel,
                onNavigateToCheckout = { price, currency, title, desc ->
                    navController.navigate(Screen.Checkout(
                        price = price, 
                        currency = currency,
                        orderTitle = title,
                        orderDescription = desc
                    ))
                }
            )
        }
        
        composable<Screen.Checkout> { backStackEntry ->
            val checkoutRoute = backStackEntry.toRoute<Screen.Checkout>()
            // We can now use checkoutRoute.price and checkoutRoute.currency if the SDK supported it!
            
            // Instantiate the SDK's ViewModel scoped to THIS specific navigation entry,
            // so returning to this screen creates a fresh state instead of the old success state.
            val factory = CheckoutViewModelFactory(checkoutSdk)
            val checkoutViewModel = ViewModelProvider(backStackEntry, factory)[CheckoutViewModel::class.java]
            
            CheckoutScreen(
                amount = checkoutRoute.price,
                currency = checkoutRoute.currency,
                orderTitle = checkoutRoute.orderTitle,
                orderDescription = checkoutRoute.orderDescription,
                viewModel = checkoutViewModel,
                themeConfig = checkoutThemeConfig,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
