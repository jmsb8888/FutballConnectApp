package com.task.futballconnectapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.task.futballconnectapp.ui.theme.FutballConnectAppTheme
import com.task.futballconnectapp.ui.theme.LoginScreen
import com.task.futballconnectapp.ui.theme.MyApplicationTheme
import com.task.futballconnectapp.ui.theme.UserRegistrationScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Aquí se aplica el tema
            MyApplicationTheme {
                // Aquí se define la interfaz de usuario
                UserRegistrationScreen()
            }
        }
    }
}

