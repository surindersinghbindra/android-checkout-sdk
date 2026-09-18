package com.caribeanroyal.headless.navigation

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.caribeanroyal.ecommercesample.feature.booking.ui.BookingScreen
import com.caribeanroyal.ecommercesample.feature.booking.ui.CruiseDetailScreen
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModel
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModelFactory

fun NavGraphBuilder.bookingGraph(
    navController: NavController,
    viewModelStoreOwner: ViewModelStoreOwner,
    bookingFactory: BookingViewModelFactory
) {
    composable("booking") {
        val bookingViewModel = ViewModelProvider(viewModelStoreOwner, bookingFactory)[BookingViewModel::class.java]
        
        BookingScreen(
            viewModel = bookingViewModel,
            onNavigateToCruiseDetail = { packageCode ->
                navController.navigate("cruiseDetail/$packageCode")
            }
        )
    }
    
    composable("cruiseDetail/{packageCode}") { backStackEntry ->
        val packageCode = backStackEntry.arguments?.getString("packageCode") ?: ""
        val bookingViewModel = ViewModelProvider(viewModelStoreOwner, bookingFactory)[BookingViewModel::class.java]

        CruiseDetailScreen(
            packageCode = packageCode,
            viewModel = bookingViewModel,
            onNavigateBack = { navController.popBackStack() },
            onNavigateToCheckout = { price, currency, title, desc ->
                navController.navigate("checkout/$price/$currency/${java.net.URLEncoder.encode(title, "UTF-8")}/${java.net.URLEncoder.encode(desc, "UTF-8")}")
            }
        )
    }
}
