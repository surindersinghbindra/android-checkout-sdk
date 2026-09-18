@file:OptIn(ExperimentalMaterial3Api::class)
package com.caribeanroyal.ecommercesample.feature.booking.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingState
import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModel

@Composable
fun BookingScreen(
    viewModel: BookingViewModel,
    onNavigateToCheckout: (Double, String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var showRoomSheet by remember { mutableStateOf(false) }

    when (val currentState = state) {
        is BookingState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is BookingState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Error: ${currentState.message}", color = MaterialTheme.colorScheme.error)
            }
        }
        is BookingState.Success -> {
            val cruise = currentState.itinerary
            
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(
                    title = { Text(cruise.title) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text("Departure: ${cruise.departurePort}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Text("Sail Date: ${cruise.sailDate}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Itinerary", style = MaterialTheme.typography.titleMedium)
                    }
                    
                    items(cruise.days) { day ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(12.dp)) {
                                Text("Day ${day.dayNumber}: ", fontWeight = FontWeight.Bold)
                                Text(day.location)
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { showRoomSheet = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Text("Room Type: ${currentState.selectedRoom}", fontWeight = FontWeight.Bold)
                                Text("Change", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total: £${currentState.finalPrice}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Button(onClick = { onNavigateToCheckout(currentState.finalPrice, cruise.currency) }) {
                            Text("Book Now")
                        }
                    }
                }
            }

            if (showRoomSheet) {
                ModalBottomSheet(onDismissRequest = { showRoomSheet = false }) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                        Text("Select Room Type", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        currentState.roomPrices.keys.forEach { room ->
                            Row(
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                RadioButton(
                                    selected = (room == currentState.selectedRoom),
                                    onClick = {
                                        viewModel.selectRoom(room)
                                        showRoomSheet = false
                                    }
                                )
                                Text("$room (+£${currentState.roomPrices[room]})", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}
