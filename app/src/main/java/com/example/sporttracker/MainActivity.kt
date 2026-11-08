package com.example.sporttracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.sporttracker.ui.ScheduleScreen
import com.example.sporttracker.ui.theme.SportTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SportTrackerTheme {
                ScheduleScreen()
            }
        }
    }
}
