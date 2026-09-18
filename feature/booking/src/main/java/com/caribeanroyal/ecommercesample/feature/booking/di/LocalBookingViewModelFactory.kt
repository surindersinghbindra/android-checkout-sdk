package com.caribeanroyal.ecommercesample.feature.booking.di

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModelProvider

val LocalBookingViewModelFactory = compositionLocalOf<ViewModelProvider.Factory> { 
    error("BookingViewModelFactory not provided by host application") 
}
