package com.caribeanroyal.headless

import android.app.Application
import com.caribeanroyal.headless.di.AppComponent
import com.caribeanroyal.headless.di.DaggerAppComponent

class HeadlessApp : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}
