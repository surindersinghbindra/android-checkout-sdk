package com.caribeanroyal.ecommercesample

import android.app.Application
import com.caribeanroyal.ecommercesample.di.AppComponent
import com.caribeanroyal.ecommercesample.di.DaggerAppComponent

import com.caribeanroyal.ecommercesample.feature.booking.di.BookingComponentProvider

class ECommerceApp : Application(), BookingComponentProvider {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }

    override fun bookingViewModelFactory() = appComponent.bookingViewModelFactory()
}
