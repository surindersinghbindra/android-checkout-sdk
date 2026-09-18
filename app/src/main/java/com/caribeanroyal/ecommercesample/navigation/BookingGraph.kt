package com.caribeanroyal.ecommercesample.navigation

import androidx.lifecycle.ViewModelProvider
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import com.caribeanroyal.ecommercesample.ECommerceApp
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.caribeanroyal.ecommercesample.core.domain.usecase.SearchCruisesUseCase
import com.caribeanroyal.ecommercesample.feature.booking.ui.BookingScreen
import com.caribeanroyal.ecommercesample.feature.booking.ui.CruiseDetailScreen
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModel
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModelFactory

fun NavGraphBuilder.bookingGraph(
    navController: NavController,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    composable<Screen.Booking> {
        val context = LocalContext.current
        val appComponent = (context.applicationContext as ECommerceApp).appComponent
        val searchCruisesUseCase = remember { appComponent.searchCruisesUseCase() }
        val bookingFactory = remember { BookingViewModelFactory(searchCruisesUseCase) }
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
        val context = LocalContext.current
        val appComponent = (context.applicationContext as ECommerceApp).appComponent
        val searchCruisesUseCase = remember { appComponent.searchCruisesUseCase() }
        val bookingFactory = remember { BookingViewModelFactory(searchCruisesUseCase) }
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
}
