package com.caribeanroyal.ecommercesample.feature.booking.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.caribeanroyal.ecommercesample.feature.booking.di.BookingComponentProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.caribeanroyal.ecommercesample.core.domain.model.CruiseItinerary
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingState
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CruiseDetailScreen(
    packageCode: String,
    onNavigateBack: () -> Unit,
    onNavigateToCheckout: (Double, String, String, String) -> Unit
) {
    val context = LocalContext.current
    val factory = remember { 
        (context.applicationContext as BookingComponentProvider).bookingViewModelFactory() 
    }
    val viewModel: BookingViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsState()
    var isIncludedExpanded by remember { mutableStateOf(true) }

    when (val currentState = state) {
        is BookingState.Success -> {
            val cruise = currentState.itineraries.find { it.packageCode == packageCode }
            if (cruise != null) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("") },
                            navigationIcon = {
                                IconButton(onClick = onNavigateBack) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                        )
                    },
                    bottomBar = {
                        Surface(
                            shadowElevation = 8.dp,
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            PaddingValues(16.dp)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Total Price", style = MaterialTheme.typography.labelMedium)
                                    Text(
                                        text = "${cruise.currency} ${cruise.basePrice}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Button(
                                    onClick = { 
                                        val desc = "Departure: ${cruise.departurePort}\nDate: ${cruise.sailDate}"
                                        onNavigateToCheckout(cruise.basePrice, cruise.currency, cruise.title, desc) 
                                    },
                                    modifier = Modifier.padding(start = 16.dp)
                                ) {
                                    Text("Continue to Checkout")
                                }
                            }
                        }
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = paddingValues.calculateBottomPadding())
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Header Image
                        if (cruise.imageUrl.isNotEmpty()) {
                            AsyncImage(
                                model = cruise.imageUrl,
                                contentDescription = cruise.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp)
                            )
                        }

                        Column(modifier = Modifier.padding(24.dp)) {
                            // Title & Favorite
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = cruise.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = { /* TODO */ },
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                                        .size(48.dp)
                                ) {
                                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite")
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))

                            // Details Grid
                            DetailRow("Leaving from", cruise.departurePort)
                            DetailRow("Onboard", "Allure of the Seas") // Mocked ship name
                            DetailRow("Dates", cruise.sailDate) // Usually would show start > end
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "View Ports",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

                            // Included Section
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Included in your cruise",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = { isIncludedExpanded = !isIncludedExpanded }) {
                                    Icon(
                                        if (isIncludedExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Toggle Included",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                            
                            if (isIncludedExpanded) {
                                Spacer(modifier = Modifier.height(8.dp))
                                BulletPoint("Delicious dining options for every meal")
                                BulletPoint("Thrilling onboard activities")
                                BulletPoint("Non-stop entertainment, live shows and comedy")
                                BulletPoint("Award-winning youth programs")
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "View all that's included",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            
                            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
                            
                            // Room Selection
                            Text(
                                text = "Room Selection",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            DetailRow("Guests", "2 Adults")
                            
                            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
                            
                            // Savings
                            Text(
                                text = "Savings",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "These are the deals you've snagged so far:",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            
                            Text("Fall Frenzy Sale", color = Color(0xFFC2185B), modifier = Modifier.padding(vertical = 4.dp))
                            Text("All Short Sailings Kicker", color = Color(0xFFC2185B), modifier = Modifier.padding(vertical = 4.dp))
                            Text("60% Off Second Guest", color = Color(0xFFC2185B), modifier = Modifier.padding(vertical = 4.dp))
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Callout Cards
                            CalloutCard("You're eligible for 3rd & 4th Guests Sail Free", Color(0xFFC2185B), true)
                            Spacer(modifier = Modifier.height(16.dp))
                            CalloutCard("You've saved $373.00 on this reservation!", Color(0xFFC2185B), false)
                            
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Cruise not found.")
                }
            }
        }
        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun BulletPoint(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "•",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun CalloutCard(text: String, color: Color, showInfo: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = color,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            if (showInfo) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = "Info",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
