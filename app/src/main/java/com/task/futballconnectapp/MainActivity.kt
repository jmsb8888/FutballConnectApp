package com.task.futballconnectapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.task.futballconnectapp.ui.MyApplicationTheme
import com.task.futballconnectapp.ui.UserRegistrationScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                UserRegistrationScreen()
            }
        }
    }
}

