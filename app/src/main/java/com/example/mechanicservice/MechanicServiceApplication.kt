package com.example.mechanicservice

import android.app.Application
import com.example.mechanicservice.data.SessionManager

class MechanicServiceApplication : Application() {

    lateinit var sessionManager: SessionManager
        private set

    override fun onCreate() {
        super.onCreate()

        sessionManager = SessionManager(this)
    }
}