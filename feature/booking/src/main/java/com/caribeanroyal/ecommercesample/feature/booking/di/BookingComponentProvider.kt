package com.caribeanroyal.ecommercesample.feature.booking.di

import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModelFactory

interface BookingComponentProvider {
    fun bookingViewModelFactory(): BookingViewModelFactory
}
