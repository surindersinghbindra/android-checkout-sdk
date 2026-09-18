@file:OptIn(ExperimentalMaterial3Api::class)
package com.caribeanroyal.ecommercesample.feature.booking.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingState
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModel

@Composable
fun BookingScreen(
    viewModel: BookingViewModel,
    onNavigateToCheckout: (Double, String, String, String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Find a Cruise") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { padding ->
        when (val currentState = state) {
            is BookingState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is BookingState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text("Error: ${currentState.message}", color = MaterialTheme.colorScheme.error)
                }
            }
            is BookingState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(currentState.itineraries) { cruise ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column {
                                if (cruise.imageUrl.isNotEmpty()) {
                                    AsyncImage(
                                        model = cruise.imageUrl,
                                        contentDescription = cruise.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxWidth().height(160.dp)
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().height(160.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("No Image")
                                    }
                                }

                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = cruise.title,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Departs: ${cruise.departurePort}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Sail Date: ${cruise.sailDate}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "From",
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                            Text(
                                                text = "${cruise.currency} ${cruise.basePrice}",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        Button(onClick = { 
                                            val desc = "Departure: ${cruise.departurePort}\nDate: ${cruise.sailDate}"
                                            onNavigateToCheckout(cruise.basePrice, cruise.currency, cruise.title, desc) 
                                        }) {
                                            Text("Start Booking")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
