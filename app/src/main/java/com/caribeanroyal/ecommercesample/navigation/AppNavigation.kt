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
import com.caribeanroyal.ecommercesample.feature.booking.ui.CruiseDetailScreen
import com.caribeanroyal.ecommercesample.sdk.checkout.core.CheckoutSdk
import com.caribeanroyal.ecommercesample.sdk.checkout.ui.CheckoutScreen
import com.caribeanroyal.ecommercesample.sdk.checkout.ui.CheckoutThemeConfig
import com.caribeanroyal.ecommercesample.sdk.checkout.ui.CheckoutViewModel
import com.caribeanroyal.ecommercesample.sdk.checkout.ui.CheckoutViewModelFactory
import androidx.navigation.toRoute
import com.caribeanroyal.ecommercesample.core.domain.usecase.SearchCruisesUseCase
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModel
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModelFactory
import com.caribeanroyal.ecommercesample.sdk.checkout.ui.CheckoutStepsConfig
import androidx.compose.runtime.remember

@Composable
fun AppNavigation(
    checkoutSdk: CheckoutSdk,
    checkoutThemeConfig: CheckoutThemeConfig,
    viewModelStoreOwner: ViewModelStoreOwner,
    searchCruisesUseCase: SearchCruisesUseCase,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // Instantiate a shared BookingViewModel factory for both Booking & Detail screens
    val bookingFactory = BookingViewModelFactory(searchCruisesUseCase)

    NavHost(
        navController = navController,
        startDestination = Screen.Booking,
        modifier = modifier
    ) {
        composable<Screen.Booking> { backStackEntry ->
            val bookingViewModel =
                ViewModelProvider(viewModelStoreOwner, bookingFactory)[BookingViewModel::class.java]

            BookingScreen(
                viewModel = bookingViewModel,
                onNavigateToCruiseDetail = { packageCode ->
                    navController.navigate(Screen.CruiseDetail(packageCode = packageCode))
                }
            )
        }

        composable<Screen.CruiseDetail> { backStackEntry ->
            val detailRoute = backStackEntry.toRoute<Screen.CruiseDetail>()
            val bookingViewModel =
                ViewModelProvider(viewModelStoreOwner, bookingFactory)[BookingViewModel::class.java]

            CruiseDetailScreen(
                packageCode = detailRoute.packageCode,
                viewModel = bookingViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCheckout = { price, currency, title, desc ->
                    navController.navigate(
                        Screen.Checkout(
                            price = price,
                            currency = currency,
                            orderTitle = title,
                            orderDescription = desc
                        )
                    )
                }
            )
        }

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
}
