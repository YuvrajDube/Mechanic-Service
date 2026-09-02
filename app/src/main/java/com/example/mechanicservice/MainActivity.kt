package com.example.mechanicservice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mechanicservice.navigation.AppNavigation
import com.example.mechanicservice.ui.theme.MechanicServiceTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MechanicServiceTheme {
                AppNavigation()
            }
        }
    }
}