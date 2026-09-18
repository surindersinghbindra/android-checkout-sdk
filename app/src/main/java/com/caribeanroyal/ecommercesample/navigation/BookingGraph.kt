package com.caribeanroyal.ecommercesample.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.caribeanroyal.ecommercesample.core.domain.usecase.SearchCruisesUseCase
import com.caribeanroyal.ecommercesample.feature.booking.ui.BookingScreen
import com.caribeanroyal.ecommercesample.feature.booking.ui.CruiseDetailScreen

fun NavGraphBuilder.bookingGraph(
    navController: NavController
) {
    composable<Screen.Booking> {


        BookingScreen(

            onNavigateToCruiseDetail = { packageCode ->
                navController.navigate(Screen.CruiseDetail(packageCode = packageCode))
            })
    }

    composable<Screen.CruiseDetail> { backStackEntry ->
        val detailRoute = backStackEntry.toRoute<Screen.CruiseDetail>()

        CruiseDetailScreen(
            packageCode = detailRoute.packageCode,

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
            })
    }
}
