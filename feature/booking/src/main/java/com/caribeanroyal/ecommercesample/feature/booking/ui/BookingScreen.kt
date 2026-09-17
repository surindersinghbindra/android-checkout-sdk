package com.caribeanroyal.ecommercesample.feature.booking.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caribeanroyal.ecommercesample.designsystem.components.PrimaryButton

@Composable
fun BookingScreen(
    onNavigateToCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Booking Screen")
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            text = "Go to Checkout",
            onClick = onNavigateToCheckout
        )
    }
}
