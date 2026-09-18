package com.caribeanroyal.headless

import android.app.Application
import com.caribeanroyal.headless.di.AppComponent
import com.caribeanroyal.headless.di.DaggerAppComponent

import com.caribeanroyal.ecommercesample.feature.booking.di.BookingComponentProvider

class HeadlessApp : Application(), BookingComponentProvider {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }

    override fun bookingViewModelFactory() = appComponent.bookingViewModelFactory()
}
