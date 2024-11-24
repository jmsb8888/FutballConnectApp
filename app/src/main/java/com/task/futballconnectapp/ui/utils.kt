package com.task.futballconnectapp.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Typography


@Composable
fun inputFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF4CAF50),
        unfocusedBorderColor = Color(0xFF4CAF50).copy(alpha = 0.5f),
        focusedLabelColor = Color(0xFF4CAF50)
    )
}

@Composable
fun MyApplicationTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(),
        typography = Typography(),
        content = content
    )
}