package com.task.futballconnectapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.task.futballconnectapp.data.viewmodel.DataViewModel
import com.task.futballconnectapp.data.viewmodel.SharedPreferencesViewModel
import com.task.futballconnectapp.ui.MyApp
import com.task.futballconnectapp.ui.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val sharedPreferencesViewModel: SharedPreferencesViewModel by viewModels()
    private val dataViewModel: DataViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                MyApp(sharedPreferencesViewModel, dataViewModel)
            }
        }
    }
}
