package com.caribeanroyal.ecommercesample

import android.app.Application
import com.caribeanroyal.ecommercesample.di.AppComponent
import com.caribeanroyal.ecommercesample.di.DaggerAppComponent

class ECommerceApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}
