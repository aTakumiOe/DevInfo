package com.access_company.android.devinfo

import android.app.Application
import com.google.firebase.FirebaseApp

class DevInfoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}