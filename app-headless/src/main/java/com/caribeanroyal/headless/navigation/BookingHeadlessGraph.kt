package com.caribeanroyal.headless.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.caribeanroyal.ecommercesample.feature.booking.ui.BookingScreen
import com.caribeanroyal.ecommercesample.feature.booking.ui.CruiseDetailScreen

fun NavGraphBuilder.bookingGraph(
    navController: NavController
) {
    composable("booking") {

        
        BookingScreen(
            
            onNavigateToCruiseDetail = { packageCode ->
                navController.navigate("cruiseDetail/$packageCode")
            }
        )
    }
    
    composable("cruiseDetail/{packageCode}") { backStackEntry ->
        val packageCode = backStackEntry.arguments?.getString("packageCode") ?: ""

        CruiseDetailScreen(
            packageCode = packageCode,
            
            onNavigateBack = { navController.popBackStack() },
            onNavigateToCheckout = { price, currency, title, desc ->
                navController.navigate("checkout/$price/$currency/${java.net.URLEncoder.encode(title, "UTF-8")}/${java.net.URLEncoder.encode(desc, "UTF-8")}")
            }
        )
    }
}
